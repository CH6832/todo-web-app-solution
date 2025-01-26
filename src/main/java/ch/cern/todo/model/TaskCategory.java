package ch.cern.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a task category in the Todo application.
 * Categories are used to organize and group related tasks.
 *
 * Features:
 * - Unique category names
 * - Bidirectional relationship with tasks
 * - JSON serialization control
 * - Lazy loading of tasks
 */
@Entity
@Table(name = "TASK_CATEGORIES")
public class TaskCategory {

    /**
     * Unique identifier for the category.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    /**
     * Name of the category.
     * Must be unique and non-null.
     */
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Column(nullable = false, unique = true)
    private String categoryName;

    /**
     * Optional description of the category.
     */
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Column
    private String categoryDescription;

    /**
     * Set of tasks belonging to this category.
     * JsonIgnore prevents infinite recursion in JSON serialization.
     * Lazy loading improves performance.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    /**
     * Default constructor required by JPA.
     */
    public TaskCategory() {
    }

    /**
     * Constructor with required fields.
     */
    public TaskCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.categoryName = categoryName;
    }

    /**
     * Constructor with all fields.
     */
    public TaskCategory(String categoryName, String categoryDescription) {
        this(categoryName);  // Reuse validation from other constructor
        this.categoryDescription = categoryDescription;
    }

    /**
     * Gets the category ID.
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Gets the category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Gets the category description.
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Gets the set of tasks in this category.
     */
    public Set<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the category ID.
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Sets the category name.
     */
    public void setCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.categoryName = categoryName;
    }

    /**
     * Sets the category description.
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * Sets the tasks for this category.
     */
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks != null ? tasks : new HashSet<>();
    }

    /**
     * Adds a task to this category and establishes the bidirectional relationship.
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        tasks.add(task);
        task.setCategory(this);
    }

    /**
     * Removes a task from this category and breaks the bidirectional relationship.
     */
    public void removeTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        tasks.remove(task);
        task.setCategory(null);
    }

    /**
     * Returns a string representation of the category.
     * Includes ID, name, description, and number of tasks.
     */
    @Override
    public String toString() {
        return "TaskCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", tasksCount=" + (tasks != null ? tasks.size() : 0) +
                '}';
    }

    /**
     * Checks if this category is equal to another object.
     * Equality is based on the categoryId field.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskCategory that = (TaskCategory) o;
        return categoryId != null && categoryId.equals(that.categoryId);
    }

    /**
     * Generates a hash code for this category.
     * Based on the categoryId field.
     */
    @Override
    public int hashCode() {
        return categoryId != null ? categoryId.hashCode() : 0;
    }
}