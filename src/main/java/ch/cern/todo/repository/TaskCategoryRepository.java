package ch.cern.todo.repository;

import ch.cern.todo.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for TaskCategory entity operations.
 * Extends JpaRepository to provide standard CRUD operations for TaskCategory entities.
 *
 * Features:
 * - Standard CRUD operations inherited from JpaRepository
 * - Automatic query generation
 * - Transaction management
 */
@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

    // Basic CRUD operations are inherited from JpaRepository

    // Optionals are here:
    /**
     * Finds a category by its name.
     */
    // Optional<TaskCategory> findByCategoryName(String name);

    /**
     * Finds categories containing the given text in their name.
     */
    // List<TaskCategory> findByCategoryNameContainingIgnoreCase(String text);

    /**
     * Checks if a category with the given name exists.
     */
    // boolean existsByCategoryName(String name);
}