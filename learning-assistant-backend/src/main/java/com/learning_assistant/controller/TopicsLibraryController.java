package com.learning_assistant.controller;

import com.learning_assistant.model.dto.TopicDTO;
import com.learning_assistant.model.viewObject.TopicLibraryVO;
import com.learning_assistant.service.TopicLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics-library")
@Slf4j
public class TopicsLibraryController {

    private final TopicLibraryService topicLibraryService;

    @Autowired
    public TopicsLibraryController(TopicLibraryService topicLibraryService) {
        this.topicLibraryService = topicLibraryService;
    }

    @GetMapping("/get-topics-by-user-id")
    public ResponseEntity<List<TopicLibraryVO>> getTopicsByUserId(@RequestParam("user-id") Long userId) {
        log.info("Get user topics by user id: {}", userId);
        return ResponseEntity.ok(topicLibraryService.getTopicsByUserId(userId));
    }

    @PostMapping("/create-topic")
    public ResponseEntity<TopicLibraryVO> createTopic (@RequestBody TopicDTO topicDTO) {

        log.info("Create topic, {}", topicDTO);

        // Save new topic
        Long topicId = topicLibraryService.createTopic(topicDTO);

        // Return new topic
        TopicLibraryVO topicLibraryVO = topicLibraryService.getTopicById(topicId);
        return ResponseEntity.ok(topicLibraryVO);
    }

}
