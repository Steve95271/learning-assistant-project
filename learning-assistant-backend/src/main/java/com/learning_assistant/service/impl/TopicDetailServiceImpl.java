package com.learning_assistant.service.impl;

import com.learning_assistant.exception.ResourceNotFoundException;
import com.learning_assistant.model.dto.UpdateTopicDTO;
import com.learning_assistant.model.entity.Conversation;
import com.learning_assistant.model.entity.SummaryNote;
import com.learning_assistant.model.entity.Topic;
import com.learning_assistant.model.entity.TopicFile;
import com.learning_assistant.model.viewObject.TopicDetailVO;
import com.learning_assistant.repository.ConversationRepository;
import com.learning_assistant.repository.SummaryNoteRepository;
import com.learning_assistant.repository.TopicFileRepository;
import com.learning_assistant.repository.TopicRepository;
import com.learning_assistant.service.TopicDetailService;
import com.learning_assistant.service.converter.TopicDetailVOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementation of TopicDetailService responsible for retrieving and assembling
 * detailed information about topics including conversations, files, and summary notes.
 */
@Service
@RequiredArgsConstructor
public class TopicDetailServiceImpl implements TopicDetailService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final TopicRepository topicRepository;
    private final TopicFileRepository topicFileRepository;
    private final SummaryNoteRepository summaryNoteRepository;
    private final ConversationRepository conversationRepository;
    private final TopicDetailVOConverter topicDetailVOConverter;

    /**
     * Retrieves detailed information about a topic by its ID.
     * This method performs multiple database queries in a read-only transaction
     * to ensure data consistency.
     *
     * @param id the unique identifier of the topic
     * @return TopicDetailVO containing all topic details
     * @throws ResourceNotFoundException if the topic is not found
     */
    @Override
    @Transactional(readOnly = true)
    public TopicDetailVO getTopicDetailById(Long id) {
        // Fetch the topic (required - throw exception if not found)
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", id));

        // Fetch related data
        List<Conversation> conversations = conversationRepository.findConversationsByTopicId(id);
        SummaryNote summaryNote = summaryNoteRepository.findByTopicId(id);
        List<TopicFile> files = topicFileRepository.findFilesByTopicId(id);

        // Build and return the view object using the converter
        return TopicDetailVO.builder()
                .topicTitle(topic.getName())
                .topicDescription(topic.getDescription())
                .conversations(topic.getConversationCount())
                .numberOfFiles(topic.getFileCount())
                .lastUpdated(topic.getUpdatedAt())
                .sessionSummaries(topicDetailVOConverter.toSessionSummaries(conversations))
                .summaryNotePreview(topicDetailVOConverter.toSummaryNotePreview(summaryNote))
                .fileInfoPreviews(topicDetailVOConverter.toFileInfoPreviews(files))
                .build();
    }

    /**
     * Updates a topic's name and description.
     * This method performs an update operation in a write transaction.
     *
     * @param topicId the unique identifier of the topic to be updated
     * @param updateDTO the DTO containing the updated name and description
     * @return TopicDetailVO containing the updated topic details
     * @throws ResourceNotFoundException if the topic is not found
     */
    @Override
    @Transactional
    public TopicDetailVO updateTopic(Long topicId, UpdateTopicDTO updateDTO) {
        // Fetch existing topic
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", topicId));

        // Update fields
        if (updateDTO.getName() != null && !updateDTO.getName().isBlank()) {
            topic.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            topic.setDescription(updateDTO.getDescription());
        }

        // Update timestamp
        topic.setUpdatedAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));

        // Save (without markNew() - will perform UPDATE)
        topic.setIsNew(false);
        topicRepository.save(topic);

        // Return updated topic detail view
        return getTopicDetailById(topicId);
    }

    /**
     * Soft deletes a topic by setting its deleted_at timestamp and status to 'deleted'.
     * This method performs a soft delete operation in a write transaction.
     *
     * @param topicId the unique identifier of the topic to be deleted
     * @throws ResourceNotFoundException if the topic is not found
     */
    @Override
    @Transactional
    public void deleteTopic(Long topicId) {
        // Fetch existing topic
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", topicId));

        // Set soft delete timestamp
        topic.setDeletedAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));

        // Mark as deleted status
        topic.setStatus(Topic.TopicStatus.deleted);

        // Update timestamp
        topic.setUpdatedAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));

        // Save
        topic.setIsNew(false);
        topicRepository.save(topic);
    }

}
