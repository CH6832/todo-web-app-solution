package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.exception.TaskAccessDeniedException;
import ch.cern.todo.exception.TaskNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling security-related operations in the Todo application.
 * Provides methods for authorization checks and access control.
 *
 * Features:
 * - Task ownership verification
 * - Admin role verification
 * - Access control checks
 * - Security context integration
 */
@Service
public class SecurityService {

    /**
     * Repository for task-related database operations.
     * Used for verifying task ownership and existence.
     */
    private final TaskRepository taskRepository;

    /**
     * Constructs a new SecurityService with the required TaskRepository.
     */
    public SecurityService(TaskRepository taskRepository) {
        if (taskRepository == null) {
            throw new IllegalArgumentException("TaskRepository cannot be null");
        }
        this.taskRepository = taskRepository;
    }

    /**
     * Checks if the current user is either the owner of the task or has admin privileges.
     * Uses the SecurityContext to get the current authentication.
     */
    public boolean isOwnerOrAdmin(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new SecurityException("No authentication present");
        }

        // Check if user is admin
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        // If not admin, check if user owns the task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        return task.getUser().getUsername().equals(auth.getName());
    }

    /**
     * Verifies if the provided authentication has access to the specified task.
     * Checks both admin privileges and task ownership.
     */
    public boolean hasAccessToTask(Long taskId, Authentication authentication) {
        if (taskId == null || authentication == null) {
            throw new IllegalArgumentException("TaskId and authentication cannot be null");
        }

        // Admin can access all tasks
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        // Regular users can only access their own tasks
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        return task.getUser().getUsername().equals(authentication.getName());
    }

    /**
     * Verifies if the current user has admin privileges.
     */
    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Validates task access and throws an exception if access is denied.
     */
    public void validateTaskAccess(Long taskId) {
        if (!isOwnerOrAdmin(taskId)) {
            throw new TaskAccessDeniedException("Access denied to task: " + taskId);
        }
    }

    /**
     * Gets the current authenticated username.
     */
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new SecurityException("No authentication present");
        }
        return auth.getName();
    }
}