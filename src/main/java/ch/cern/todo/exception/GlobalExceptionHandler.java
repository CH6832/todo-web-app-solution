package ch.cern.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Todo application.
 * Provides centralized exception handling across all controllers.
 *
 * Features:
 * - Handles resource not found exceptions
 * - Handles access denied scenarios
 * - Handles validation failures
 * - Provides generic error handling
 * - Converts exceptions to appropriate HTTP responses
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and converts it to a NOT_FOUND response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse() {
            @Override
            public HttpStatusCode getStatusCode() {
                return HttpStatus.NOT_FOUND;
            }

            @Override
            public ProblemDetail getBody() {
                ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problemDetail.setDetail(ex.getMessage());
                return problemDetail;
            }
        };
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles AccessDeniedException and converts it to a FORBIDDEN response.
     * Triggered when a user attempts to access unauthorized resources.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
    }

    /**
     * Handles all unhandled exceptions and converts them to INTERNAL_SERVER_ERROR responses.
     * Acts as a catch-all for any exceptions not specifically handled elsewhere.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        // Log the exception details here
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An internal server error occurred. Please try again later.");
    }

    /**
     * Handles validation exceptions and converts them to BAD_REQUEST responses.
     * Processes field-level validation errors and returns them in a structured format.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}