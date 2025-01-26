package ch.cern.todo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Standard error response class for the Todo application.
 * Provides a consistent structure for error responses across the API.
 *
 * Features:
 * - HTTP status code
 * - Error message
 * - Timestamp of the error
 * - JSON serialization support
 */
public class ErrorResponse {

    /**
     * HTTP status code of the error.
     */
    private int status;

    /**
     * Descriptive message about the error.
     * Should be user-friendly and informative.
     */
    private String message;

    /**
     * Timestamp when the error occurred.
     * Formatted in ISO-8601 date-time format.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Default constructor.
     * Initializes timestamp to current date-time.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs an ErrorResponse with all fields.
     */
    public ErrorResponse(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    /**
     * Gets the HTTP status code.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp of when the error occurred.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Creates a string representation of the error response.
     */
    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * Creates a builder for ErrorResponse.
     */
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    /**
     * Builder class for creating ErrorResponse instances.
     */
    public static class ErrorResponseBuilder {
        private int status;
        private String message;
        private LocalDateTime timestamp = LocalDateTime.now();

        /**
         * Sets the status code.
         */
        public ErrorResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the error message.
         */
        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the timestamp.
         */
        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Builds the ErrorResponse instance.
         */
        public ErrorResponse build() {
            ErrorResponse response = new ErrorResponse();
            response.setStatus(this.status);
            response.setMessage(this.message);
            response.setTimestamp(this.timestamp);
            return response;
        }
    }
}