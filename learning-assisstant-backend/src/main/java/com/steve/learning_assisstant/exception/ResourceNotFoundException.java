package com.steve.learning_assisstant.exception;

/**
 * Exception thrown when a requested resource is not found in the database.
 * This is a runtime exception that can be used to trigger appropriate HTTP 404 responses.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, Long id) {
        super(String.format("%s not found with id: %d", resourceType, id));
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
