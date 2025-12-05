package com.learning_assistant.exception;

/**
 * Exception thrown when file validation fails (invalid type, size, etc.).
 */
public class FileValidationException extends RuntimeException {

    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
