package ch.cern.todo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a user in the Todo application.
 * This class manages user information, roles, and associated tasks.
 *
 * Features:
 * - Unique username constraint
 * - Password encryption (handled at service layer)
 * - Role-based authorization
 * - Bidirectional relationship with tasks
 * - JPA entity mappings
 */
@Entity
@Table(name = "USERS")
public class User {

    /**
     * Unique identifier for the user.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username of the user.
     * Must be unique and non-null.
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Encrypted password of the user.
     * Should never be stored in plain text.
     */
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    /**
     * Set of roles assigned to the user.
     * Stored in a separate table with eager fetching.
     * Examples: ROLE_USER, ROLE_ADMIN
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    /**
     * Set of tasks owned by the user.
     * Bidirectional relationship with Task entity.
     */
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Task> tasks = new HashSet<>();

    /**
     * Gets the user's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user's ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    /**
     * Gets the encrypted password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * Note: Password should be encrypted before setting.
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    /**
     * Sets the user's roles.
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles != null ? roles : new HashSet<>();
    }

    /**
     * Gets the user's roles.
     * Returns an empty set if roles is null.
     */
    public Set<String> getRoles() {
        return roles != null ? roles : new HashSet<>();
    }

    /**
     * Gets the user's tasks.
     */
    public Set<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the user's tasks.
     */
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks != null ? tasks : new HashSet<>();
    }

    /**
     * Adds a task to the user's task set.
     */
    public void addTask(Task task) {
        tasks.add(task);
        task.setUser(this);
    }

    /**
     * Removes a task from the user's task set.
     */
    public void removeTask(Task task) {
        tasks.remove(task);
        task.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}