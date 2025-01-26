package ch.cern.todo.exception;

/**
 * Custom exception class for handling resource not found scenarios in the Todo application.
 * This exception is thrown when attempting to access or manipulate a resource that doesn't exist.
 *
 * Use cases include:
 * - Task not found by ID
 * - Category not found by ID
 * - User not found by username
 * - Invalid resource references
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and ID.
     */
    public static ResourceNotFoundException forResource(String resourceType, Long id) {
        return new ResourceNotFoundException(String.format("%s not found with id: %d", resourceType, id));
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and field.
     */
    public static ResourceNotFoundException forResourceWithField(String resourceType,
                                                                 String fieldName,
                                                                 String fieldValue) {
        return new ResourceNotFoundException(
                String.format("%s not found with %s: %s", resourceType, fieldName, fieldValue)
        );
    }
}