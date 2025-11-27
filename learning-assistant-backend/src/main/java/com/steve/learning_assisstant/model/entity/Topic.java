package com.steve.learning_assisstant.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic implements Persistable<Long> {

    @Id
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Integer conversationCount;
    private Integer fileCount;
    private TopicStatus status;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /*
        Using save method with Spring Data JDBC
        when custom primary key is presented
        we need to mark this is a new object
        otherwise Spring Data JDBC will treat save as update
    */
    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markNew() {
        this.isNew = true;
    }

    private LocalDateTime deletedAt;

    public enum TopicStatus {
        active,
        deleted
    }

}
