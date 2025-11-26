package com.steve.learning_assisstant.controller;

import com.steve.learning_assisstant.model.dto.TopicDTO;
import com.steve.learning_assisstant.model.viewObject.TopicLibraryVO;
import com.steve.learning_assisstant.service.TopicLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topicsLibrary")
@Slf4j
public class TopicsLibraryController {

    private final TopicLibraryService topicLibraryService;

    @Autowired
    public TopicsLibraryController(TopicLibraryService topicLibraryService) {
        this.topicLibraryService = topicLibraryService;
    }

    @GetMapping("/getTopicsByUserId")
    public ResponseEntity<List<TopicLibraryVO>> getTopicsByUserId(@RequestParam("userId") Long userId) {
        log.info("Get user topics by user id: {}", userId);
        return ResponseEntity.ok(topicLibraryService.getTopicsByUserId(userId));
    }

    @PostMapping("/createTopic")
    public ResponseEntity<TopicLibraryVO> createTopic (@RequestBody TopicDTO topicDTO) {

        log.info("Create topic, {}", topicDTO);

        // Save new topic
        Long topicId = topicLibraryService.createTopic(topicDTO);

        // Return new topic
        TopicLibraryVO topicLibraryVO = topicLibraryService.getTopicById(topicId);
        return ResponseEntity.ok(topicLibraryVO);
    }

}
