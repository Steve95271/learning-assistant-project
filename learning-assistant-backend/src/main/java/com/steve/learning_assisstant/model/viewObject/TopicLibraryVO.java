package com.steve.learning_assisstant.model.viewObject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.steve.learning_assisstant.model.entity.Topic;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TopicLibraryVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String title;
    private String description;
    private String lastUpdated;
    private Integer filesCount;
    private Integer conversationsCount;
    private List<String> filePreviews;

    public static TopicLibraryVO fromTopicToTopicLibraryView(Topic topic) {
        return builder()
                .id(topic.getId())
                .title(topic.getName())
                .description(topic.getDescription())
                .lastUpdated(topic.getUpdatedAt().toString())
                .filesCount(topic.getFileCount())
                .conversationsCount(topic.getConversationCount())
                // TODO Add file preview icons
                .filePreviews(List.of())
                .build();
    }

}
