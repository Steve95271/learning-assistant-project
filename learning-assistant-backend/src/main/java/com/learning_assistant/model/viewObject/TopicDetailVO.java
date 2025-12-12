package com.learning_assistant.model.viewObject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDetailVO {

    private String topicTitle;
    private String topicDescription;
    private Integer conversations;
    private Integer numberOfFiles;
    private LocalDateTime lastUpdated;
    private List<SessionSummary> sessionSummaries;
    private SummaryNotePreview summaryNotePreview;
    private List<FileInfoPreview> fileInfoPreviews;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionSummary {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String title;
        private String preview;
        private String lastChat;
        private Integer messageCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryNotePreview {
        private String summaryNoteTitle;
        private Integer numberOfConversations;
        private Boolean autoUpdateEnabled;
        private LocalDateTime lastUpdated;
        private Integer wordCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfoPreview {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long fileId;
        private String icon;
        private String fileName;
        private Long size;
    }
}
