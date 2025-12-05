package com.learning_assistant.model.dto;

import lombok.Data;

@Data
public class ConfirmUploadDTO {

    private Long fileId;
    private Boolean success;
    private String errorMessage;  // Optional, for failure cases
}
