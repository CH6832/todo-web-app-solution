package ch.cern.todo.service;

import ch.cern.todo.model.User;
import ch.cern.todo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for user-related operations in the Todo application.
 * Provides functionality for user management and authentication-related operations.
 *
 * This service implements transactional behavior with read-only by default to optimize
 * database performance and ensure data consistency.
 *
 * Key features:
 * - User retrieval based on authentication
 * - Transactional operations
 * - Integration with Spring Security
 * - User validation and verification
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    /**
     * Repository for user-related database operations.
     * Initialized through constructor injection for better testability.
     * This repository handles all database interactions for user entities.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the required UserRepository dependency.
     * Uses constructor injection as per Spring best practices for better testability
     * and immutability.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the currently authenticated user from the database.
     * This method validates the authentication object and fetches the corresponding
     * user entity based on the username in the authentication details.
     */
    public User getCurrentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
