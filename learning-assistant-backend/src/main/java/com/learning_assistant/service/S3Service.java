package com.learning_assistant.service;

/**
 * Service interface for Amazon S3 operations including pre-signed URL generation.
 */
public interface S3Service {

    /**
     * Generates a pre-signed URL for uploading a file to S3.
     *
     * @param storageKey the S3 object key where the file will be stored
     * @param contentType the MIME type of the file
     * @param expirationMinutes the number of minutes until the URL expires
     * @return the pre-signed URL string
     * @throws com.learning_assistant.exception.S3ServiceException if URL generation fails
     */
    String generatePresignedUploadUrl(String storageKey, String contentType, int expirationMinutes);

    /**
     * Constructs a unique S3 storage key with UUID and file extension.
     * Format: learning_assistant_files/{UUID}.{extension}
     *
     * @param fileType the MIME type of the file (e.g., "application/pdf")
     * @return the constructed storage key
     */
    String constructStorageKey(String fileType);
}
