package ch.cern.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for task responses in the Todo application.
 * This class represents the task data that is sent back to clients,
 * providing a simplified and secure view of task information.
 *
 * Features:
 * - Task identification
 * - Basic task details
 * - Category information
 * - User assignment
 * - Deadline management
 * - JSON formatting
 */
public class TaskResponseDTO {

    /**
     * Unique identifier of the task.
     */
    @NotNull(message = "Task ID cannot be null")
    private Long taskId;

    /**
     * Name/title of the task.
     */
    @NotNull(message = "Task name cannot be null")
    private String taskName;

    /**
     * Detailed description of the task.
     */
    private String taskDescription;

    /**
     * Deadline for task completion.
     * Formatted as ISO-8601 date-time.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Deadline cannot be null")
    private LocalDateTime deadline;

    /**
     * Name of the category this task belongs to.
     */
    @NotNull(message = "Category name cannot be null")
    private String categoryName;

    /**
     * Username of the task owner.
     */
    @NotNull(message = "Username cannot be null")
    private String username;

    /**
     * Default constructor.
     */
    public TaskResponseDTO() {
    }

    /**
     * Constructs a TaskResponseDTO with all required fields.
     */
    public TaskResponseDTO(Long taskId, String taskName, String taskDescription,
                           LocalDateTime deadline, String categoryName, String username) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.categoryName = categoryName;
        this.username = username;
    }

    /**
     * Gets the task ID.
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * Sets the task ID.
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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
        this.deadline = deadline;
    }

    /**
     * Gets the category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the category name.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        this.username = username;
    }

    /**
     * Creates a string representation of the TaskResponseDTO.
     */
    @Override
    public String toString() {
        return "TaskResponseDTO{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", deadline=" + deadline +
                ", categoryName='" + categoryName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    /**
     * Checks equality between this TaskResponseDTO and another object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskResponseDTO)) return false;
        TaskResponseDTO that = (TaskResponseDTO) o;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(deadline, that.deadline);
    }

    /**
     * Generates a hash code for this TaskResponseDTO.
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskId, taskName, deadline);
    }

    /**
     * Creates a builder for TaskResponseDTO.
     */
    public static TaskResponseDTOBuilder builder() {
        return new TaskResponseDTOBuilder();
    }

    /**
     * Builder class for creating TaskResponseDTO instances.
     */
    public static class TaskResponseDTOBuilder {
        private Long taskId;
        private String taskName;
        private String taskDescription;
        private LocalDateTime deadline;
        private String categoryName;
        private String username;

        /**
         * Sets the task ID.
         *
         * @param taskId the task ID
         * @return the builder instance
         */
        public TaskResponseDTOBuilder taskId(Long taskId) {
            this.taskId = taskId;
            return this;
        }

        /**
         * Sets the task name.
         */
        public TaskResponseDTOBuilder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        /**
         * Sets the task description.
         */
        public TaskResponseDTOBuilder taskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
            return this;
        }

        /**
         * Sets the deadline.
         */
        public TaskResponseDTOBuilder deadline(LocalDateTime deadline) {
            this.deadline = deadline;
            return this;
        }

        /**
         * Sets the category name.
         */
        public TaskResponseDTOBuilder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        /**
         * Sets the username.
         */
        public TaskResponseDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Builds the TaskResponseDTO instance.
         */
        public TaskResponseDTO build() {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setTaskId(this.taskId);
            dto.setTaskName(this.taskName);
            dto.setTaskDescription(this.taskDescription);
            dto.setDeadline(this.deadline);
            dto.setCategoryName(this.categoryName);
            dto.setUsername(this.username);
            return dto;
        }
    }
}