package ch.cern.todo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for creating and updating tasks in the Todo application.
 * This class represents the task data received from clients, containing only the
 * necessary fields for task creation and modification.
 *
 * Features:
 * - Input validation
 * - Future date validation for deadlines
 * - Required field validation
 * - Size constraints
 * - JSON formatting
 */
public class TaskDTO {

    /**
     * Name/title of the task.
     * Must not be blank and should be between 1 and 100 characters.
     */
    @NotBlank(message = "Task name is required")
    @Size(min = 1, max = 100, message = "Task name must be between 1 and 100 characters")
    private String taskName;

    /**
     * Detailed description of the task.
     * Optional field with maximum length of 500 characters.
     */
    @Size(max = 500, message = "Task description cannot exceed 500 characters")
    private String taskDescription;

    /**
     * Deadline for task completion.
     * Must be a future date/time.
     */
    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;

    /**
     * ID of the category this task belongs to.
     * Must not be null.
     */
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    /**
     * Default constructor.
     */
    public TaskDTO() {
    }

    /**
     * Constructs a TaskDTO with all required fields.
     */
    public TaskDTO(String taskName, LocalDateTime deadline, Long categoryId) {
        this.taskName = taskName;
        this.deadline = deadline;
        this.categoryId = categoryId;
    }

    /**
     * Gets the task name.
     */
    public String getTaskName() {
        return taskName;
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
     * Gets the task description.
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * Sets the task description.
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * Gets the task deadline.
     */
    public LocalDateTime getDeadline() {
        return deadline;
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
     * Gets the category ID.
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID.
     */
    public void setCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        this.categoryId = categoryId;
    }

    /**
     * Returns a string representation of the TaskDTO.
     */
    @Override
    public String toString() {
        return "TaskDTO{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", deadline=" + deadline +
                ", categoryId=" + categoryId +
                '}';
    }

    /**
     * Checks equality between this TaskDTO and another object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDTO)) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return Objects.equals(taskName, taskDTO.taskName) &&
                Objects.equals(deadline, taskDTO.deadline) &&
                Objects.equals(categoryId, taskDTO.categoryId);
    }

    /**
     * Generates a hash code for this TaskDTO.
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskName, deadline, categoryId);
    }

    /**
     * Creates a builder for TaskDTO.
     */
    public static TaskDTOBuilder builder() {
        return new TaskDTOBuilder();
    }

    /**
     * Builder class for creating TaskDTO instances.
     */
    public static class TaskDTOBuilder {
        private String taskName;
        private String taskDescription;
        private LocalDateTime deadline;
        private Long categoryId;

        public TaskDTOBuilder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public TaskDTOBuilder taskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
            return this;
        }

        public TaskDTOBuilder deadline(LocalDateTime deadline) {
            this.deadline = deadline;
            return this;
        }

        public TaskDTOBuilder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public TaskDTO build() {
            TaskDTO dto = new TaskDTO();
            dto.setTaskName(taskName);
            dto.setTaskDescription(taskDescription);
            dto.setDeadline(deadline);
            dto.setCategoryId(categoryId);
            return dto;
        }
    }
}