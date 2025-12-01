package com.learning_assistant.repository;

import com.learning_assistant.model.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepositoryCustom{

    private final JdbcTemplate jdbcTemplate;

    public void insertTopic(Topic topic) {
        String sql = """
                INSERT INTO topics (id, user_id, name, description, conversation_count, file_count, status, last_accessed_at, created_at, updated_at, deleted_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                topic.getId(),
                topic.getUserId(),
                topic.getName(),
                topic.getDescription(),
                topic.getConversationCount(),
                topic.getFileCount(),
                topic.getStatus() != null ? topic.getStatus().name() : "active",
                null,
                topic.getCreatedAt(),
                topic.getUpdatedAt(),
                null
        );
    }
}
