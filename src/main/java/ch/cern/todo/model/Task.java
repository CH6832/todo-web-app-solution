package ch.cern.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

/**
 * Entity class representing a task in the Todo application.
 * Tasks are the core entities of the application, representing work items
 * that need to be completed by users.
 *
 * Features:
 * - Unique task identification
 * - Category classification
 * - User assignment
 * - Deadline management
 * - JSON serialization control
 */
@Entity
@Table(name = "TASKS")
public class Task {

    /**
     * Unique identifier for the task.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    /**
     * Name/title of the task.
     * Required field that cannot be null.
     */
    @NotBlank(message = "Task name is required")
    @Size(min = 1, max = 100, message = "Task name must be between 1 and 100 characters")
    @Column(nullable = false)
    private String taskName;

    /**
     * Detailed description of the task.
     * Optional field providing additional information.
     */
    @Size(max = 500, message = "Task description cannot exceed 500 characters")
    @Column
    private String taskDescription;

    /**
     * Deadline for task completion.
     * Required field indicating when the task should be completed.
     */
    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    @Column(nullable = false)
    private LocalDateTime deadline;

    /**
     * Category of the task.
     * Required field establishing relationship with TaskCategory.
     * Eager fetching ensures category is always loaded with task.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("tasks")
    private TaskCategory category;

    /**
     * User assigned to the task.
     * Required field establishing relationship with User.
     * Eager fetching ensures user is always loaded with task.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("tasks")
    private User user;

    /**
     * Default constructor required by JPA.
     */
    public Task() {
    }

    /**
     * Constructor with all fields.
     */
    public Task(String taskName, String taskDescription, LocalDateTime deadline,
                TaskCategory category, User user) {
        if (taskName == null || deadline == null || category == null || user == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.category = category;
        this.user = user;
    }

    /**
     * Gets the task ID.
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * Gets the task name.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Gets the task description.
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * Gets the task deadline.
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Gets the task category.
     */
    public TaskCategory getCategory() {
        return category;
    }

    /**
     * Gets the assigned user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the task ID.
     * @param taskId the ID to set
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * Sets the task name.
     */
    public void setTaskName(String taskName) {
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        this.taskName = taskName;
    }

    /**
     * Sets the task description.
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * Sets the task deadline.
     */
    public void setDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException("Deadline cannot be null");
        }
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }
        this.deadline = deadline;
    }

    /**
     * Sets the task category.
     */
    public void setCategory(TaskCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    /**
     * Sets the assigned user.
     */
    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    /**
     * Returns a string representation of the task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", deadline=" + deadline +
                ", category=" + (category != null ? category.getCategoryName() : "null") +
                ", user=" + (user != null ? user.getUsername() : "null") +
                '}';
    }

    /**
     * Checks if this task is equal to another object.
     * Equality is based on the taskId field.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId != null && taskId.equals(task.taskId);
    }

    /**
     * Generates a hash code for this task.
     * Based on the taskId field.
     */
    @Override
    public int hashCode() {
        return taskId != null ? taskId.hashCode() : 0;
    }
}