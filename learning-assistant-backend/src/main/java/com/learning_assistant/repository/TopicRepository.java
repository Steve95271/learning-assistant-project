package com.learning_assistant.repository;

import com.learning_assistant.model.entity.Topic;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long>, TopicRepositoryCustom {

    @Query("""
                SELECT *
                FROM topics
                WHERE user_id = :userId
                AND status = 'active'
                AND deleted_at IS NULL
            """)
    List<Topic> findActiveTopicsByUserId(@Param("userId") Long userId);

}
