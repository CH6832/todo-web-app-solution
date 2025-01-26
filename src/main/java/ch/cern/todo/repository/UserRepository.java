package ch.cern.todo.repository;

import ch.cern.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Extends JpaRepository to provide standard CRUD operations and custom queries for User entities.
 *
 * Features:
 * - Standard CRUD operations inherited from JpaRepository
 * - Custom query method for username-based user lookup
 * - Spring Data JPA implementation
 * - Automatic query generation
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     * This method is automatically implemented by Spring Data JPA based on the method name.
     * Returns an Optional to handle cases where the user might not exist.
     */
    Optional<User> findByUsername(String username);
}
