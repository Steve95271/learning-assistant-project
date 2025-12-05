package com.learning_assistant.model.dto;

import lombok.Data;

@Data
public class FileUploadRequestDTO {

    private Long topicId;
    private Long userId;
    private String filename;
    private String fileType;  // MIME type
    private Long fileSize;    // Size in bytes
}
