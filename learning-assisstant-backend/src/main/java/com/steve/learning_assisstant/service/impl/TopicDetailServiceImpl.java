package com.steve.learning_assisstant.service.impl;

import com.steve.learning_assisstant.exception.ResourceNotFoundException;
import com.steve.learning_assisstant.model.entity.Conversation;
import com.steve.learning_assisstant.model.entity.SummaryNote;
import com.steve.learning_assisstant.model.entity.Topic;
import com.steve.learning_assisstant.model.entity.TopicFile;
import com.steve.learning_assisstant.model.viewObject.TopicDetailVO;
import com.steve.learning_assisstant.repository.*;
import com.steve.learning_assisstant.service.TopicDetailService;
import com.steve.learning_assisstant.service.converter.TopicDetailVOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
