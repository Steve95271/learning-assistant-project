package com.steve.learning_assisstant.repository;

import com.steve.learning_assisstant.model.entity.Conversation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Long> {

    @Query("""
                SELECT *
                FROM conversations
                WHERE topic_id = :topicId
                AND deleted_at IS NULL
                ORDER BY updated_at DESC
            """)
    List<Conversation> findConversationsByTopicId(@Param("topicId") Long topicId);

    @Query("""
                SELECT *
                FROM conversations
                WHERE user_id = :userId
                AND deleted_at IS NULL
                ORDER BY updated_at DESC
            """)
    List<Conversation> findConversationsByUserId(@Param("userId") Long userId);

    @Query("""
                SELECT *
                FROM conversations
                WHERE id = :id
                AND deleted_at IS NULL
            """)
    Optional<Conversation> findActiveConversationById(@Param("id") Long id);

    @Query("""
                SELECT COUNT(*)
                FROM conversations
                WHERE topic_id = :topicId
                AND deleted_at IS NULL
            """)
    Integer countConversationsByTopicId(@Param("topicId") Long topicId);

}
