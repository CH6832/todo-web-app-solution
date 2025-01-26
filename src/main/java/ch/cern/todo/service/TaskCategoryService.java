package ch.cern.todo.service;

import ch.cern.todo.dto.CategoryDTO;
import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class responsible for managing task categories in the Todo application.
 * Provides CRUD operations for task categories with transaction management.
 *
 * Features:
 * - Category creation, retrieval, update, and deletion
 * - Data transfer object (DTO) handling
 * - Transactional operations
 * - Input validation
 */
@Service
@Transactional
public class TaskCategoryService {
    private final TaskCategoryRepository categoryRepository;

    /**
     * Constructs a new TaskCategoryService with the required repository.
     */
    public TaskCategoryService(TaskCategoryRepository categoryRepository) {
        if (categoryRepository == null) {
            throw new IllegalArgumentException("CategoryRepository cannot be null");
        }
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new task category from the provided DTO.
     * Validates and transforms the DTO into a TaskCategory entity.
     */
    public TaskCategory createCategory(CategoryDTO dto) {
        validateCategoryDTO(dto);

        TaskCategory category = new TaskCategory();
        category.setCategoryName(dto.getCategoryName());
        category.setCategoryDescription(dto.getCategoryDescription());
        return categoryRepository.save(category);
    }

    /**
     * Retrieves a category by its ID.
     */
    public TaskCategory getCategory(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    /**
     * Validates the CategoryDTO for required fields and data consistency.
     */
    private void validateCategoryDTO(CategoryDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CategoryDTO cannot be null");
        }
        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
    }

    /**
     * Updates an existing category with new information from the DTO.
     */
    public TaskCategory updateCategory(Long id, CategoryDTO dto) {
        validateCategoryDTO(dto);

        TaskCategory category = getCategory(id);
        category.setCategoryName(dto.getCategoryName());
        category.setCategoryDescription(dto.getCategoryDescription());
        return categoryRepository.save(category);
    }

    /**
     * Deletes a category by its ID.
     * Verifies existence before deletion to provide better error messaging.
     */
    public void deleteCategory(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Retrieves all available task categories.
     */
    public List<TaskCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}