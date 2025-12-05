package com.learning_assistant.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("topic_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicFile implements Persistable<Long> {

    @Id
    private Long id;
    private Long topicId;
    private Long userId;
    private String filename;
    private String icon;
    private String fileType;
    private Long fileSize;
    private String storageKey;
    private FileStatus status;
    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    /*
        Using save method with Spring Data JDBC
        when custom primary key is presented
        we need to mark this is a new object
        otherwise Spring Data JDBC will treat save as update
    */
    @Transient
    private Boolean isNew;

    public enum FileStatus {
        pending,
        uploaded
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markNew() {
        this.isNew = true;
    }

}
