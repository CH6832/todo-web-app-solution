package ch.cern.todo.repository;

import ch.cern.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Task entity operations.
 * Extends JpaRepository to provide standard CRUD operations and custom queries for Task entities.
 *
 * Features:
 * - Standard CRUD operations inherited from JpaRepository
 * - Custom search functionality with multiple criteria
 * - Case-insensitive search capabilities
 * - Flexible parameter handling
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Performs a flexible search for tasks based on multiple optional criteria.
     * All parameters are optional and can be combined for advanced filtering.
     * The search is case-insensitive for text fields.
     */
    @Query("SELECT t FROM Task t WHERE " +
            "(:username IS NULL OR t.user.username = :username) AND " +
            "(:name IS NULL OR LOWER(t.taskName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:description IS NULL OR LOWER(t.taskDescription) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:deadline IS NULL OR CAST(t.deadline AS date) = CAST(:deadline AS date)) AND " +
            "(:categoryId IS NULL OR t.category.categoryId = :categoryId)")
    List<Task> search(
            @Param("username") String username,
            @Param("name") String name,
            @Param("description") String description,
            @Param("deadline") LocalDateTime deadline,
            @Param("categoryId") Long categoryId);
}
