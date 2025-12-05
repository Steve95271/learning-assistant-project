package com.learning_assistant.exception;

/**
 * Exception thrown when S3 service operations fail.
 */
public class S3ServiceException extends RuntimeException {

    public S3ServiceException(String message) {
        super(message);
    }

    public S3ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
