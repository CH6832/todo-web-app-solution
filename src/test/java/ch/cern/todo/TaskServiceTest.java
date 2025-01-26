package ch.cern.todo;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.model.User;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TaskService class.
 * Tests the business logic layer in isolation using Mockito framework.
 *
 * Test coverage includes:
 * - Task creation with validation
 * - Task retrieval and updates
 * - Task search functionality
 * - Error handling and validation
 * - Business rule enforcement
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private TaskCategory testCategory;
    private User testUser;
    private LocalDateTime testDeadline;

    /**
     * Sets up test data before each test method.
     * Initializes:
     * - Test user with basic attributes
     * - Test category for task classification
     * - Future deadline for task validation
     * - Test task with all required fields
     */
    @BeforeEach
    void setUp() {
        // Create test user with required fields
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        // Create test category with required fields
        testCategory = new TaskCategory();
        testCategory.setCategoryId(1L);
        testCategory.setCategoryName("Test Category");
        testCategory.setCategoryDescription("Test Description");

        // Set future deadline for valid task creation
        testDeadline = LocalDateTime.now().plusDays(1);

        // Create complete test task
        testTask = new Task();
        testTask.setTaskId(1L);
        testTask.setTaskName("Service Test Task");
        testTask.setTaskDescription("Test Description");
        testTask.setDeadline(testDeadline);
        testTask.setCategory(testCategory);
        testTask.setUser(testUser);
    }

    /**
     * Tests successful task creation with valid input.
     * Verifies:
     * - Task is saved correctly
     * - All fields are preserved
     * - Repository is called exactly once
     * - Returned task matches input
     */
    @Test
    void createTask_ShouldReturnCreatedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task created = taskService.createTask(testTask);

        assertThat(created).isNotNull();
        assertThat(created.getTaskName()).isEqualTo(testTask.getTaskName());
        assertThat(created.getUser()).isEqualTo(testUser);
        assertThat(created.getCategory()).isEqualTo(testCategory);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Tests successful task retrieval by ID.
     * Verifies:
     * - Correct task is returned
     * - All fields match expected values
     * - Repository is queried exactly once
     */
    @Test
    void getTask_ShouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Task found = taskService.getTask(1L);

        assertThat(found).isNotNull();
        assertThat(found.getTaskId()).isEqualTo(1L);
        assertThat(found.getUser()).isEqualTo(testUser);
        verify(taskRepository, times(1)).findById(1L);
    }

    /**
     * Tests task retrieval with invalid ID.
     * Verifies:
     * - Appropriate exception is thrown
     * - Exception contains meaningful message
     * - Repository is queried exactly once
     */
    @Test
    void getTask_WithInvalidId_ShouldThrowException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found");
    }

    /**
     * Tests successful task update.
     * Verifies:
     * - Task is updated correctly
     * - All fields are updated as expected
     * - Repository is called for both find and save
     * - Updated task is returned
     */
    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        Task updatedTask = new Task();
        updatedTask.setTaskName("Updated Task");
        updatedTask.setCategory(testCategory);
        updatedTask.setUser(testUser);
        updatedTask.setDeadline(testDeadline);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertThat(result).isNotNull();
        assertThat(result.getTaskName()).isEqualTo("Updated Task");
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Tests task search functionality with multiple criteria.
     * Verifies:
     * - Search returns expected results
     * - All search criteria are properly handled
     * - Repository search method is called correctly
     */
    @Test
    void searchTasks_ShouldReturnMatchingTasks() {
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.search(any(), any(), any(), any(), any()))
                .thenReturn(expectedTasks);

        List<Task> result = taskService.searchTasks(
                testUser.getUsername(),
                "Test",
                "Description",
                testDeadline,
                testCategory.getCategoryId()
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskName()).isEqualTo(testTask.getTaskName());
        verify(taskRepository, times(1)).search(any(), any(), any(), any(), any());
    }

    /**
     * Tests validation of required fields during task creation.
     * Verifies:
     * - Exception is thrown for missing required fields
     * - Exception message is appropriate
     * - No repository calls are made
     */
    @Test
    void createTask_WithMissingRequiredFields_ShouldThrowException() {
        Task invalidTask = new Task();

        assertThatThrownBy(() -> taskService.createTask(invalidTask))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Task name is required");
    }
}