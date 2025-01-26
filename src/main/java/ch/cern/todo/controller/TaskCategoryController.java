package ch.cern.todo.controller;

import ch.cern.todo.dto.CategoryDTO;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing task categories in the Todo application.
 * Provides endpoints for CRUD operations on categories with role-based access control.
 *
 * Features:
 * - Category creation and management
 * - Admin-only operations
 * - Input validation
 * - Secure endpoints
 */
@RestController
@RequestMapping("/api/categories")
public class TaskCategoryController {

    /**
     * Service for handling category operations.
     */
    private final TaskCategoryService categoryService;

    /**
     * Constructs TaskCategoryController with required service.
     */
    public TaskCategoryController(TaskCategoryService categoryService) {
        if (categoryService == null) {
            throw new IllegalArgumentException("CategoryService cannot be null");
        }
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category.
     * Only accessible to users with ADMIN role.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskCategory> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    /**
     * Retrieves a specific category by ID.
     * Accessible to all authenticated users.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskCategory> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    /**
     * Updates an existing category.
     * Only accessible to users with ADMIN role.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskCategory> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    /**
     * Deletes a category.
     * Only accessible to users with ADMIN role.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all categories.
     * Accessible to all authenticated users.
     */
    @GetMapping
    public ResponseEntity<List<TaskCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}