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

@Table("summary_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryNote implements Persistable<Long> {

    @Id
    private Long id;
    private Long topicId;
    private Long userId;
    private String title;
    private String content;
    private String contentSections;
    private Integer wordCount;
    private Integer estimatedReadTimeMinutes;
    private GenerationSource generationSource;
    private Boolean autoUpdateEnabled;
    private Integer sourceConversationCount;
    private LocalDateTime lastRegeneratedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

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

    public enum GenerationSource {
        auto_first,
        auto_update,
        manual
    }

}
