package com.steve.learning_assisstant.service;

import com.steve.learning_assisstant.model.dto.TopicDTO;
import com.steve.learning_assisstant.model.viewObject.TopicLibraryVO;

import java.util.List;

public interface TopicLibraryService {

    List<TopicLibraryVO> getTopicsByUserId(Long userId);

    Long createTopic(TopicDTO topicDTO);

    TopicLibraryVO getTopicById(Long topicId);
}
