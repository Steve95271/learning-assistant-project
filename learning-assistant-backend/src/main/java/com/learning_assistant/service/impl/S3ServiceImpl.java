package com.learning_assistant.service.impl;

import com.learning_assistant.config.S3Config;
import com.learning_assistant.exception.S3ServiceException;
import com.learning_assistant.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Presigner s3Presigner;
    private final S3Config s3Config;
    private final S3Client s3Client;

    private static final String STORAGE_PREFIX = "learning_assistant_files";


    @Override
    public String generatePresignedUploadUrl(String storageKey, String contentType, int expirationMinutes) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(storageKey)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expirationMinutes))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            String presignedUrl = presignedRequest.url().toString();
            log.info("Generated pre-signed URL for storage key: {}, expires in {} minutes", storageKey, expirationMinutes);

            return presignedUrl;

        } catch (Exception e) {
            log.error("Failed to generate pre-signed URL for storage key: {}", storageKey, e);
            throw new S3ServiceException("Failed to generate pre-signed URL: " + e.getMessage(), e);
        }
    }

    @Override
    public String constructStorageKey(String fileType) {
        String extension = getFileExtension(fileType);
        String uuid = UUID.randomUUID().toString();
        return String.format("%s/%s%s", STORAGE_PREFIX, uuid, extension);
    }

    @Override
    public void deleteObject(String storageKey) {
        try {
            DeleteObjectRequest s3DeleteRequest = DeleteObjectRequest
                    .builder()
                    .bucket(s3Config.getBucketName())
                    .key(storageKey).build();

            s3Client.deleteObject(s3DeleteRequest);
            log.info("Successfully deleted S3 Object: {}", storageKey);
        } catch (Exception e) {
            log.error("Failed to delete S3 object: {}", storageKey, e);
            throw new S3ServiceException("Failed to delete file from S3: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts file extension from MIME type.
     *
     * @param fileType the MIME type
     * @return file extension with leading dot (e.g., ".pdf")
     */
    private String getFileExtension(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "application/pdf" -> ".pdf";
            case "text/plain" -> ".txt";
            case "text/markdown" -> ".md";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx";
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/heic" -> ".heic";
            default -> "";
        };
    }
}
