package ch.cern.todo;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.User;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the SecurityService class.
 * Tests the security-related business logic using Mockito for isolation.
 *
 * Test coverage includes:
 * - Admin access verification
 * - Task ownership verification
 * - Authorization combinations
 * - Security context handling
 */
@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SecurityService securityService;

    private Authentication authentication;
    private SecurityContext securityContext;
    private Task testTask;
    private User testUser;

    /**
     * Sets up test environment before each test.
     * Initializes:
     * - Security context and authentication mocks
     * - Test user with basic attributes
     * - Test task with owner relationship
     * - Security context holder configuration
     *
     * This setup ensures proper security context for each test.
     */
    @BeforeEach
    void setUp() {
        // Configure security context mocks
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Initialize test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Initialize test task with owner
        testTask = new Task();
        testTask.setTaskId(1L);
        testTask.setUser(testUser);
    }

    /**
     * Tests that admin users have access to any task.
     * Verifies:
     * - Admin role grants access regardless of ownership
     * - Admin authority is properly recognized
     * - Security context is properly consulted
     */
    @Test
    void isOwnerOrAdmin_WithAdmin_ShouldReturnTrue() {
        // Setup admin authority
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        doReturn(Collections.singleton(adminAuthority)).when(authentication).getAuthorities();

        // Verify admin access
        boolean result = securityService.isOwnerOrAdmin(1L);
        assertThat(result).isTrue();

        // Verify security context was consulted
        verify(authentication).getAuthorities();
    }

    /**
     * Tests that task owners have access to their tasks.
     * Verifies:
     * - Task ownership grants access
     * - User role is properly handled
     * - Username matching works correctly
     */
    @Test
    void isOwnerOrAdmin_WithOwner_ShouldReturnTrue() {
        // Setup user authority and authentication
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");
        doReturn(Collections.singleton(userAuthority)).when(authentication).getAuthorities();
        when(authentication.getName()).thenReturn("testuser");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Verify owner access
        boolean result = securityService.isOwnerOrAdmin(1L);
        assertThat(result).isTrue();

        // Verify repository and authentication were consulted
        verify(taskRepository).findById(1L);
        verify(authentication).getName();
    }

    /**
     * Tests that non-owners cannot access others' tasks.
     * Verifies:
     * - Non-owners are denied access
     * - User role without ownership is insufficient
     * - Username mismatch is properly handled
     */
    @Test
    void isOwnerOrAdmin_WithNonOwner_ShouldReturnFalse() {
        // Setup user authority with different username
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");
        doReturn(Collections.singleton(userAuthority)).when(authentication).getAuthorities();
        when(authentication.getName()).thenReturn("otheruser");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Verify access is denied
        boolean result = securityService.isOwnerOrAdmin(1L);
        assertThat(result).isFalse();

        // Verify all security checks were performed
        verify(taskRepository).findById(1L);
        verify(authentication).getName();
        verify(authentication).getAuthorities();
    }
}