package com.learning_assistant.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.learning_assistant.config.S3Config;
import com.learning_assistant.exception.FileValidationException;
import com.learning_assistant.exception.ResourceNotFoundException;
import com.learning_assistant.model.dto.ConfirmUploadDTO;
import com.learning_assistant.model.dto.FileUploadRequestDTO;
import com.learning_assistant.model.dto.FileUploadResponseDTO;
import com.learning_assistant.model.entity.Topic;
import com.learning_assistant.model.entity.TopicFile;
import com.learning_assistant.repository.TopicFileRepository;
import com.learning_assistant.repository.TopicRepository;
import com.learning_assistant.service.FileUploadService;
import com.learning_assistant.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final Snowflake snowflake;
    private final S3Service s3Service;
    private final S3Config s3Config;
    private final TopicFileRepository topicFileRepository;
    private final TopicRepository topicRepository;

    // Validation constants
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes
    private static final Set<String> SUPPORTED_FILE_TYPES = Set.of(
            "application/pdf",
            "text/plain",
            "text/markdown",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/png",
            "image/jpeg",
            "image/heic"
    );

    @Override
    @Transactional
    public FileUploadResponseDTO initiateFileUpload(FileUploadRequestDTO request) {
        log.info("Initiating file upload for topic: {}, filename: {}", request.getTopicId(), request.getFilename());

        // Validate file type and size
        validateFile(request.getFileType(), request.getFileSize());

        // Verify topic exists
        topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", request.getTopicId()));

        // Generate Snowflake ID for the file
        Long fileId = snowflake.nextId();

        // Determine emoji icon based on file type
        // TODO use LLM to determine emoji in the future
        String icon = getIconForFileType(request.getFileType());

        // Construct S3 storage key
        String storageKey = s3Service.constructStorageKey(request.getFileType());

        // Generate pre-signed URL
        String presignedUrl = s3Service.generatePresignedUploadUrl(
                storageKey,
                request.getFileType(),
                s3Config.getPresignedUrlExpirationMinutes()
        );

        // Create TopicFile record with status = pending
        TopicFile topicFile = TopicFile.builder()
                .id(fileId)
                .topicId(request.getTopicId())
                .userId(request.getUserId())
                .filename(request.getFilename())
                .icon(icon)
                .fileType(request.getFileType())
                .fileSize(request.getFileSize())
                .storageKey(storageKey)
                .status(TopicFile.FileStatus.pending)  // Pending until upload confirmed
                .uploadedAt(null)  // Will be set after successful upload
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .deletedAt(null)
                .build();

        topicFile.markNew();
        topicFileRepository.save(topicFile);

        log.info("Created pending file record with id: {}, storage key: {}", fileId, storageKey);

        return FileUploadResponseDTO.builder()
                .fileId(fileId)
                .presignedUrl(presignedUrl)
                .storageKey(storageKey)
                .build();
    }

    @Override
    @Transactional
    public void confirmUpload(ConfirmUploadDTO confirmation) {
        log.info("Confirming upload for file id: {}, success: {}", confirmation.getFileId(), confirmation.getSuccess());

        TopicFile topicFile = topicFileRepository.findById(confirmation.getFileId())
                .orElseThrow(() -> new ResourceNotFoundException("TopicFile", confirmation.getFileId()));

        if (Boolean.TRUE.equals(confirmation.getSuccess())) {
            // Successful upload: update status to 'uploaded' and set uploaded_at timestamp
            topicFile.setStatus(TopicFile.FileStatus.uploaded);
            topicFile.setUploadedAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
            topicFile.setIsNew(false);
            topicFileRepository.save(topicFile);

            // Increment topic's file_count cache
            incrementTopicFileCount(topicFile.getTopicId());

            log.info("Upload confirmed successfully for file id: {}", confirmation.getFileId());
        } else {
            // Failed upload: hard delete the pending record
            topicFileRepository.deleteById(confirmation.getFileId());
            log.warn("Upload failed for file id: {}, reason: {}. Record deleted.",
                    confirmation.getFileId(), confirmation.getErrorMessage());
        }
    }

    @Override
    @Transactional
    public int cleanupStalePendingUploads(int hoursThreshold) {
        log.info("Starting cleanup of stale pending uploads older than {} hours", hoursThreshold);

        // Calculate threshold time
        LocalDateTime thresholdTime = LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId()))
                .minusHours(hoursThreshold);

        // Find stale pending uploads using status field
        List<TopicFile> staleFiles = topicFileRepository.findStalePendingUploads(thresholdTime);

        // Check collection if null
        if (CollectionUtils.isEmpty(staleFiles)) {
            log.info("No stale pending upload(s)");
            return 0;
        } else {
            int deletedCount = 0;
            for (TopicFile file : staleFiles) {
                topicFileRepository.deleteById(file.getId());
                deletedCount++;
                log.debug("Deleted stale pending upload: fileId={}, filename={}, created={}",
                        file.getId(), file.getFilename(), file.getCreatedAt());
            }

            log.info("Cleanup completed. Deleted {} stale pending upload(s)", deletedCount);
            return deletedCount;
        }


    }

    /**
     * Validates file type and size.
     *
     * @throws FileValidationException if validation fails
     */
    private void validateFile(String fileType, Long fileSize) {
        if (fileType == null || fileType.isBlank()) {
            throw new FileValidationException("File type is required");
        }

        if (!SUPPORTED_FILE_TYPES.contains(fileType.toLowerCase())) {
            throw new FileValidationException("Unsupported file type: " + fileType +
                    ". Supported types: " + String.join(", ", SUPPORTED_FILE_TYPES));
        }

        if (fileSize == null || fileSize <= 0) {
            throw new FileValidationException("File size must be greater than 0");
        }

        if (fileSize > MAX_FILE_SIZE) {
            throw new FileValidationException(String.format(
                    "File size exceeds maximum limit. Max: %d MB, Actual: %.2f MB",
                    MAX_FILE_SIZE / (1024 * 1024),
                    fileSize / (1024.0 * 1024.0)
            ));
        }
    }

    /**
     * Maps file MIME type to emoji icon.
     */
    private String getIconForFileType(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "application/pdf" -> "📕";
            case "text/plain" -> "📄";
            case "text/markdown" -> "📝";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "📘";
            case "image/png", "image/jpeg", "image/heic" -> "🖼️";
            default -> "📎";
        };
    }

    /**
     * Increments the file_count field for a topic.
     */
    private void incrementTopicFileCount(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", topicId));

        int currentCount = topic.getFileCount() != null ? topic.getFileCount() : 0;
        topic.setFileCount(currentCount + 1);
        topic.setUpdatedAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
        topic.setIsNew(false);
        topicRepository.save(topic);

        log.debug("Incremented file count for topic {}: {} -> {}", topicId, currentCount, currentCount + 1);
    }
}
