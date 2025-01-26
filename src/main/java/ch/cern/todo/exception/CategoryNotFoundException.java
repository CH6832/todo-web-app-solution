package ch.cern.todo.exception;

/**
 * Custom exception for category-related errors in the Todo application.
 * This exception is thrown when attempting to access or manipulate a category
 * that doesn't exist in the system.
 *
 * Features:
 * - Custom message support
 * - ID-based error messages
 * - Runtime exception behavior
 * - Integration with global exception handling
 */
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new CategoryNotFoundException with the specified message.
     * Use this constructor when you want to provide a custom error message.
     */
    public CategoryNotFoundException(String message) {
        super(validateMessage(message));
    }

    /**
     * Constructs a new CategoryNotFoundException with the specified category ID.
     * Creates a formatted error message including the category ID.
     */
    public CategoryNotFoundException(Long id) {
        super(createIdMessage(id));
    }

    /**
     * Constructs a new CategoryNotFoundException with the specified message and cause.
     * Use this constructor when you want to wrap another exception.
     */
    public CategoryNotFoundException(String message, Throwable cause) {
        super(validateMessage(message), cause);
    }

    /**
     * Creates a CategoryNotFoundException for a specific category name.
     * Useful when searching for categories by name.
     */
    public static CategoryNotFoundException forName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        return new CategoryNotFoundException("Category not found with name: " + categoryName);
    }

    /**
     * Validates the provided message.
     */
    private static String validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Exception message cannot be null or empty");
        }
        return message;
    }

    /**
     * Creates a formatted message for ID-based exceptions.
     */
    private static String createIdMessage(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        return String.format("Category not found with id: %d", id);
    }

    /**
     * Creates a formatted message for multiple criteria.
     */
    public static CategoryNotFoundException withCriteria(String criteria) {
        return new CategoryNotFoundException(
                String.format("Category not found with criteria: %s", criteria)
        );
    }
}