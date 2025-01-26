package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import ch.cern.todo.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing tasks in the Todo application.
 * Provides endpoints for CRUD operations and task searching.
 *
 * Features:
 * - Task creation and management
 * - Security integration
 * - Search functionality
 * - Role-based access control
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    /**
     * Constructs TaskController with required services.
     */
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    /**
     * Creates a new task for the authenticated user.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        task.setUser(userService.getCurrentUser(authentication));
        return ResponseEntity.ok(taskService.createTask(task));
    }

    /**
     * Retrieves a specific task by ID.
     * Verifies that the requesting user has permission to access the task.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId, Principal principal) {
        Task task = taskService.getTask(taskId);

        if (!task.getUser().getUsername().equals(principal.getName()) &&
                !hasRole(principal, "ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied");
        }

        return ResponseEntity.ok(task);
    }

    /**
     * Checks if the principal has a specific role.
     */
    private boolean hasRole(Principal principal, String roleName) {
        if (principal instanceof Authentication) {
            return ((Authentication) principal).getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
        }
        return false;
    }

    /**
     * Alternative endpoint for retrieving a task by ID.
     * Handles access denied scenarios gracefully.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.getTask(id));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Updates an existing task.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    /**
     * Deletes a task.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches for tasks based on multiple criteria.
     * All parameters are optional and can be combined.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(taskService.searchTasks(username, name, description, deadline, categoryId));
    }
}