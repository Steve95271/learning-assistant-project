package com.steve.learning_assisstant.service;

import com.steve.learning_assisstant.model.viewObject.TopicDetailVO;

public interface TopicDetailService {

    /**
     * Retrieves the details of a topic based on the provided topic ID.
     *
     * @param id the unique identifier of the topic to be retrieved
     * @return a TopicDetailVO object containing detailed information about the topic,
     * including its title, description, session summaries, file information,
     * and other related details
     */
    TopicDetailVO getTopicDetailById(Long id);

}
