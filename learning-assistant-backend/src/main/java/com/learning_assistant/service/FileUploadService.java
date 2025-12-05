package com.learning_assistant.service;

import com.learning_assistant.exception.FileValidationException;
import com.learning_assistant.exception.ResourceNotFoundException;
import com.learning_assistant.model.dto.ConfirmUploadDTO;
import com.learning_assistant.model.dto.FileUploadRequestDTO;
import com.learning_assistant.model.dto.FileUploadResponseDTO;

/**
 * Service interface for file upload operations.
 */
public interface FileUploadService {

    /**
     * Initiates a file upload by generating a pre-signed S3 URL and creating a pending file record.
     *
     * @param request the file upload request containing metadata
     * @return the response with fileId, presignedUrl, and storageKey
     * @throws FileValidationException if file validation fails
     * @throws ResourceNotFoundException if topic not found
     */
    FileUploadResponseDTO initiateFileUpload(FileUploadRequestDTO request);

    /**
     * Confirms the upload result (success or failure).
     * On success: sets uploaded_at timestamp and increments topic file_count.
     * On failure: hard deletes the pending file record.
     *
     * @param confirmation the confirmation DTO with fileId and success status
     * @throws ResourceNotFoundException if file record not found
     */
    void confirmUpload(ConfirmUploadDTO confirmation);

    /**
     * Cleans up stale pending uploads (files that were never uploaded).
     * Hard deletes records where uploaded_at IS NULL and created beyond the threshold.
     *
     * @param hoursThreshold the number of hours after which pending uploads are considered stale
     * @return the number of records deleted
     */
    int cleanupStalePendingUploads(int hoursThreshold);
}
