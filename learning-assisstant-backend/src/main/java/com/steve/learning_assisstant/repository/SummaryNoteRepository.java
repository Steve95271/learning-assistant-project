package com.steve.learning_assisstant.repository;

import com.steve.learning_assisstant.model.entity.SummaryNote;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryNoteRepository extends CrudRepository<SummaryNote, Long> {

    @Query("""
                SELECT *
                FROM summary_notes
                WHERE topic_id = :topicId
                AND deleted_at IS NULL
            """)
    SummaryNote findByTopicId(@Param("topicId") Long topicId);

    @Query("""
                SELECT *
                FROM summary_notes
                WHERE id = :id
                AND deleted_at IS NULL
            """)
    SummaryNote findActiveSummaryNoteById(@Param("id") Long id);

    @Query("""
                SELECT *
                FROM summary_notes
                WHERE topic_id = :topicId
                AND auto_update_enabled = true
                AND deleted_at IS NULL
            """)
    SummaryNote findAutoUpdateEnabledByTopicId(@Param("topicId") Long topicId);

}
