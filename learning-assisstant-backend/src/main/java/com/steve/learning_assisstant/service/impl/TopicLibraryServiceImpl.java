package com.steve.learning_assisstant.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.steve.learning_assisstant.model.dto.TopicDTO;
import com.steve.learning_assisstant.model.entity.Topic;
import com.steve.learning_assisstant.model.response.TopicLibraryView;
import com.steve.learning_assisstant.repository.TopicRepository;
import com.steve.learning_assisstant.service.TopicLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicLibraryServiceImpl implements TopicLibraryService {

    private final TopicRepository topicRepository;
    private final Snowflake snowflake;

    @Override
    public List<TopicLibraryView> getTopicsByUserId(Long userId) {
        // query topics by user id
        List<Topic> activeTopics = topicRepository.findActiveTopicsByUserId(userId);

        // conver to topic library view
        List<TopicLibraryView> topicLibraryViewList = new ArrayList<>();
        for (Topic topic : activeTopics) {
            topicLibraryViewList.add(TopicLibraryView.fromTopicToTopicLibraryView(topic));
        }

        return topicLibraryViewList;
    }

    @Override
    public TopicLibraryView getTopicById(Long topicId) {
        // Query
        Topic topic = topicRepository.findById(topicId).orElseThrow();

        // Convert
        return TopicLibraryView.fromTopicToTopicLibraryView(topic);
    }

    @Override
    @Transactional
    public Long createTopic(TopicDTO topicDTO) {

        // Prepare the id
        Long primaryKeyId  = snowflake.nextId();

        // Operate save to database
        topicRepository.save(Topic.builder()
                .id(primaryKeyId)
                .userId(100L)
                .name(topicDTO.getName())
                .description(topicDTO.getDescription())
                .createdAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())))
                .updatedAt(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())))
                .conversationCount(0)
                .fileCount(0)
                .status(Topic.TopicStatus.active)
                .isNew(true)
                .build()
        );

        return primaryKeyId;
    }
}
