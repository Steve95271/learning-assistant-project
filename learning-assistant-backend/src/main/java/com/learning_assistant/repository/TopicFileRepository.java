package com.learning_assistant.repository;

import com.learning_assistant.model.entity.TopicFile;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopicFileRepository extends CrudRepository<TopicFile, Long> {

    @Query("""
                SELECT *
                FROM topic_files
                WHERE topic_id = :topicId
                AND status = 'uploaded'
                AND deleted_at IS NULL
                ORDER BY uploaded_at DESC
            """)
    List<TopicFile> findFilesByTopicId(@Param("topicId") Long topicId);

    @Query("""
                SELECT *
                FROM topic_files
                WHERE storage_key = :storageKey
                AND deleted_at IS NULL
            """)
    Optional<TopicFile> findByStorageKey(@Param("storageKey") String storageKey);

    @Query("""
                SELECT COUNT(*)
                FROM topic_files
                WHERE topic_id = :topicId
                AND status = 'uploaded'
                AND deleted_at IS NULL
            """)
    Integer countFilesByTopicId(@Param("topicId") Long topicId);

    @Query("""
                SELECT *
                FROM topic_files
                WHERE status = 'pending'
                AND deleted_at IS NULL
                AND created_at < :thresholdTime
            """)
    List<TopicFile> findStalePendingUploads(@Param("thresholdTime") LocalDateTime thresholdTime);

}
