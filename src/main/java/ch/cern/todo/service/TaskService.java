package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.repository.TaskRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class responsible for managing task-related operations in the Todo application.
 * Provides CRUD operations and search functionality for tasks with security checks.
 *
 * Features:
 * - Task creation, retrieval, update, and deletion
 * - Advanced search capabilities
 * - Security validation
 * - Input validation
 * - Transactional operations
 */
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final SecurityService securityService;

    /**
     * Constructs a new TaskService with required dependencies.
     */
    public TaskService(TaskRepository taskRepository, SecurityService securityService) {
        this.taskRepository = taskRepository;
        this.securityService = securityService;
    }

    /**
     * Searches for tasks based on multiple criteria.
     * All parameters are optional and can be combined for advanced filtering.
     */
    public List<Task> searchTasks(String username, String name,
                                  String description, LocalDateTime deadline,
                                  Long categoryId) {
        return taskRepository.search(username, name, description, deadline, categoryId);
    }

    /**
     * Validates a task entity ensuring all required fields are present and valid.
     */
    private void validateTask(Task task) {
        if (task.getTaskName() == null || task.getTaskName().trim().isEmpty()) {
            throw new IllegalArgumentException("Task name is required");
        }
        if (task.getDeadline() == null || task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future");
        }
        if (task.getCategory() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        if (task.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
    }

    /**
     * Retrieves a task by its ID with security checks.
     * Only admins or task owners can access the task.
     */
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));


    }

    /**
     * Updates an existing task with security checks.
     * Only admins or task owners can update the task.
     */
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public Task updateTask(Long id, Task task) {
        Task existingTask = getTask(id);
        // Update task properties
        return taskRepository.save(existingTask);
    }

    /**
     * Deletes a task by its ID with security checks.
     * Only admins or task owners can delete the task.
     */
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Creates a new task with validation.
     * Validates all required fields before saving.
     */
    @Transactional
    public Task createTask(Task task) {
        // Validate task
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        // Validate required fields
        if (task.getTaskName() == null || task.getTaskName().trim().isEmpty()) {
            throw new IllegalArgumentException("Task name is required");
        }

        if (task.getDeadline() == null) {
            throw new IllegalArgumentException("Deadline is required");
        }

        if (task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future");
        }

        if (task.getCategory() == null) {
            throw new IllegalArgumentException("Category is required");
        }

        if (task.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }

        return taskRepository.save(task);
    }
}
