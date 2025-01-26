package ch.cern.todo.exception;

/**
 * Custom exception for task-related security errors.
 */
public class TaskAccessDeniedException extends SecurityException {

    public TaskAccessDeniedException(String message) {
        super(message);
    }
}

