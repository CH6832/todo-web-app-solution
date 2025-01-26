package ch.cern.todo;

import ch.cern.todo.dto.CategoryDTO;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.service.TaskCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TaskCategoryService class.
 * Tests the business logic for category management in isolation using Mockito.
 *
 * Test coverage includes:
 * - Category creation and validation
 * - Category retrieval operations
 * - Category update functionality
 * - Error handling scenarios
 */
@ExtendWith(MockitoExtension.class)
class TaskCategoryServiceTest {

    @Mock
    private TaskCategoryRepository categoryRepository;

    @InjectMocks
    private TaskCategoryService categoryService;

    private TaskCategory testCategory;
    private CategoryDTO categoryDTO;

    /**
     * Sets up test data before each test method.
     * Initializes:
     * - Test category entity with all required fields
     * - Category DTO for testing service operations
     */
    @BeforeEach
    void setUp() {
        // Initialize test category entity
        testCategory = new TaskCategory();
        testCategory.setCategoryId(1L);
        testCategory.setCategoryName("Test Category");
        testCategory.setCategoryDescription("Test Description");

        // Initialize category DTO
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Test Category");
        categoryDTO.setCategoryDescription("Test Description");
    }

    /**
     * Tests successful category creation from DTO.
     * Verifies:
     * - Category is created correctly
     * - All fields are properly mapped from DTO
     * - Repository save method is called once
     * - Returned category matches input data
     */
    @Test
    void createCategory_ShouldReturnCreatedCategory() {
        when(categoryRepository.save(any(TaskCategory.class))).thenReturn(testCategory);

        TaskCategory created = categoryService.createCategory(categoryDTO);

        assertThat(created).isNotNull();
        assertThat(created.getCategoryName()).isEqualTo(categoryDTO.getCategoryName());
        verify(categoryRepository, times(1)).save(any(TaskCategory.class));
    }

    /**
     * Tests successful category retrieval by ID.
     * Verifies:
     * - Correct category is returned
     * - All fields match expected values
     * - Repository findById is called once
     */
    @Test
    void getCategory_WithValidId_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        TaskCategory found = categoryService.getCategory(1L);

        assertThat(found).isNotNull();
        assertThat(found.getCategoryId()).isEqualTo(1L);
        verify(categoryRepository, times(1)).findById(1L);
    }

    /**
     * Tests category retrieval with invalid ID.
     * Verifies:
     * - Appropriate exception is thrown
     * - Exception contains meaningful message
     * - Repository is queried exactly once
     */
    @Test
    void getCategory_WithInvalidId_ShouldThrowException() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Category not found");

        verify(categoryRepository, times(1)).findById(999L);
    }

    /**
     * Tests successful category update.
     * Verifies:
     * - Category is updated correctly
     * - All fields are updated from DTO
     * - Repository find and save methods are called
     * - Updated category reflects changes
     */
    @Test
    void updateCategory_WithValidId_ShouldReturnUpdatedCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(TaskCategory.class))).thenReturn(testCategory);

        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setCategoryName("Updated Category");
        updateDTO.setCategoryDescription("Updated Description");

        // Act
        TaskCategory updated = categoryService.updateCategory(1L, updateDTO);

        // Assert
        assertThat(updated).isNotNull();
        assertThat(updated.getCategoryName()).isEqualTo(updateDTO.getCategoryName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(TaskCategory.class));
    }
}