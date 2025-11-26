package com.steve.learning_assisstant.service.converter;

import com.steve.learning_assisstant.model.entity.Conversation;
import com.steve.learning_assisstant.model.entity.SummaryNote;
import com.steve.learning_assisstant.model.entity.TopicFile;
import com.steve.learning_assisstant.model.viewObject.TopicDetailVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converter component responsible for converting entities to TopicDetailVO and its nested DTOs.
 * Centralizes conversion logic for better maintainability and testability.
 */
@Component
public class TopicDetailVOConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts a list of Conversation entities to SessionSummary DTOs.
     * Returns empty list if input is null or empty.
     *
     * @param conversations the list of Conversation entities
     * @return list of SessionSummary DTOs
     */
    public List<TopicDetailVO.SessionSummary> toSessionSummaries(List<Conversation> conversations) {
        if (conversations == null || conversations.isEmpty()) {
            return Collections.emptyList();
        }

        return conversations.stream()
                .map(this::toSessionSummary)
                .collect(Collectors.toList());
    }

    /**
     * Converts a single Conversation entity to SessionSummary DTO.
     *
     * @param conversation the Conversation entity
     * @return SessionSummary DTO
     */
    private TopicDetailVO.SessionSummary toSessionSummary(Conversation conversation) {
        return TopicDetailVO.SessionSummary.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .preview(conversation.getFirstMessagePreview())
                .lastChat(formatDateTime(conversation.getLastMessageAt()))
                .messageCount(conversation.getMessageCount())
                .build();
    }

    /**
     * Converts a SummaryNote entity to SummaryNotePreview DTO.
     * Returns null if input is null.
     *
     * @param summaryNote the SummaryNote entity
     * @return SummaryNotePreview DTO or null
     */
    public TopicDetailVO.SummaryNotePreview toSummaryNotePreview(SummaryNote summaryNote) {
        if (summaryNote == null) {
            return null;
        }

        return TopicDetailVO.SummaryNotePreview.builder()
                .summaryNoteTitle(summaryNote.getTitle())
                .numberOfConversations(summaryNote.getSourceConversationCount())
                .autoUpdateEnabled(summaryNote.getAutoUpdateEnabled())
                .lastUpdated(summaryNote.getUpdatedAt())
                .wordCount(summaryNote.getWordCount())
                .build();
    }

    /**
     * Converts a list of TopicFile entities to FileInfoPreview DTOs.
     * Returns empty list if input is null or empty.
     *
     * @param files the list of TopicFile entities
     * @return list of FileInfoPreview DTOs
     */
    public List<TopicDetailVO.FileInfoPreview> toFileInfoPreviews(List<TopicFile> files) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        return files.stream()
                .map(this::toFileInfoPreview)
                .collect(Collectors.toList());
    }

    /**
     * Converts a single TopicFile entity to FileInfoPreview DTO.
     *
     * @param file the TopicFile entity
     * @return FileInfoPreview DTO
     */
    private TopicDetailVO.FileInfoPreview toFileInfoPreview(TopicFile file) {
        return TopicDetailVO.FileInfoPreview.builder()
                .fileId(file.getId())
                .icon(file.getIcon())
                .fileName(file.getFilename())
                .size(file.getFileSize())
                .build();
    }

    /**
     * Formats a LocalDateTime to ISO-8601 string format.
     * Returns null if input is null.
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted string or null
     */
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : null;
    }
}
