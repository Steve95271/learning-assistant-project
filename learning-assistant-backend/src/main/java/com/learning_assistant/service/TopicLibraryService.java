package com.learning_assistant.service;

import com.learning_assistant.model.dto.TopicDTO;
import com.learning_assistant.model.viewObject.TopicLibraryVO;

import java.util.List;

public interface TopicLibraryService {

    List<TopicLibraryVO> getTopicsByUserId(Long userId);

    Long createTopic(TopicDTO topicDTO);

    TopicLibraryVO getTopicById(Long topicId);
}
