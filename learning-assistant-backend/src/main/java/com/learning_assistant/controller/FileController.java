package com.learning_assistant.controller;

import com.learning_assistant.model.dto.ConfirmUploadDTO;
import com.learning_assistant.model.dto.FileUploadRequestDTO;
import com.learning_assistant.model.dto.FileUploadResponseDTO;
import com.learning_assistant.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    /**
     * Initiates a file upload by generating a pre-signed S3 URL.
     *
     * @param request the file upload request with metadata
     * @return the response containing fileId, presignedUrl, and storageKey
     */
    @PostMapping("/initiate-upload")
    public ResponseEntity<FileUploadResponseDTO> initiateUpload(@RequestBody FileUploadRequestDTO request) {
        log.info("Received file upload initiation request for topic: {}, filename: {}",
                request.getTopicId(), request.getFilename());

        FileUploadResponseDTO response = fileService.initiateFileUpload(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Confirms the result of a file upload (success or failure).
     *
     * @param confirmation the confirmation DTO with fileId and success status
     * @return 204 No Content on success
     */
    @PostMapping("/confirm-upload")
    public ResponseEntity<Void> confirmUpload(@RequestBody ConfirmUploadDTO confirmation) {
        log.info("Received upload confirmation for file id: {}, success: {}",
                confirmation.getFileId(), confirmation.getSuccess());

        fileService.confirmUpload(confirmation);

        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a file by soft deleting the database record, hard deleting from S3.
     * @param fileId the file id
     * @return 204 No Content if success
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        log.info("Received file deletion request for file id: {}", fileId);
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
