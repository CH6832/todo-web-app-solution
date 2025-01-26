package ch.cern.todo;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.model.User;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for security features of the Todo application.
 * Tests the complete security flow with real HTTP requests and database operations.
 *
 * Features tested:
 * - Authentication requirements
 * - Authorization rules
 * - Task access control
 * - User role enforcement
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.sql.init.mode=never",
                "logging.level.org.springframework.security=DEBUG",
                "logging.level.org.springframework.web=DEBUG",
                "logging.level.ch.cern.todo=DEBUG",
                "spring.security.debug=true"
        }
)
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user1;
    private User user2;
    private Task task;
    private TaskCategory category;

    /**
     * Sets up test data before each test.
     * Initializes:
     * - Clean database state
     * - Test category
     * - Test users with roles
     * - Test task assigned to user1
     */
    @BeforeEach
    void setUp() {
        // Clean database state
        taskRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        // Initialize test category
        category = new TaskCategory();
        category.setCategoryName("Test Category");
        category.setCategoryDescription("Test Description");
        category = categoryRepository.save(category);

        // Create test users
        user1 = createUser("user1", "password1", "ROLE_USER");
        user2 = createUser("user2", "password2", "ROLE_USER");

        // Log user creation
        System.out.println("User1 roles: " + user1.getRoles());
        System.out.println("User2 roles: " + user2.getRoles());

        // Create test task
        task = createTask(user1);
        System.out.println("Created task with ID: " + task.getTaskId() +
                " for user: " + task.getUser().getUsername());
    }

    /**
     * Verifies that users are created with correct roles.
     * Tests the basic user setup required for other tests.
     */
    @Test
    void verifyUserSetup() {
        User foundUser1 = userRepository.findByUsername("user1")
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User foundUser2 = userRepository.findByUsername("user2")
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        assertThat(foundUser1.getRoles()).contains("ROLE_USER");
        assertThat(foundUser2.getRoles()).contains("ROLE_USER");
    }

    /**
     * Tests that unauthenticated requests are rejected.
     * Verifies basic security is working.
     */
    @Test
    void whenNoAuthentication_thenUnauthorized() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/tasks/" + task.getTaskId(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Creates a new user with specified credentials and role.
     */
    private User createUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(new HashSet<>(Collections.singleton(role)));
        return userRepository.save(user);
    }

    /**
     * Creates a new task assigned to specified user.
     */
    private Task createTask(User user) {
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setDeadline(LocalDateTime.now().plusDays(1));
        task.setCategory(category);
        task.setUser(user);
        return taskRepository.save(task);
    }

    /**
     * Creates HTTP headers with Basic Authentication.
     */
    private HttpHeaders createBasicAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}