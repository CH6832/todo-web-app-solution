package ch.cern.todo.exception;

/**
 * Custom exception for task not found errors.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}