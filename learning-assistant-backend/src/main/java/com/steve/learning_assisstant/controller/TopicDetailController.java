package com.steve.learning_assisstant.controller;

import com.steve.learning_assisstant.model.viewObject.TopicDetailVO;
import com.steve.learning_assisstant.service.TopicDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/topicDetail")
public class TopicDetailController {

    private final TopicDetailService topicDetailService;

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDetailVO> getTopicDetailById(@PathVariable Long topicId) {
        log.info("Get topic detail by id: {}", topicId);
        return ResponseEntity.ok(topicDetailService.getTopicDetailById(topicId));
    }

}
