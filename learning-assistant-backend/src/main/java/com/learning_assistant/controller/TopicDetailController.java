package com.learning_assistant.controller;

import com.learning_assistant.model.dto.UpdateTopicDTO;
import com.learning_assistant.model.viewObject.TopicDetailVO;
import com.learning_assistant.service.TopicDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/topic-detail")
public class TopicDetailController {

    private final TopicDetailService topicDetailService;

    @GetMapping("/{topic-id}")
    public ResponseEntity<TopicDetailVO> getTopicDetailById(@PathVariable("topic-id") Long topicId) {
        log.info("Get topic detail by id: {}", topicId);
        return ResponseEntity.ok(topicDetailService.getTopicDetailById(topicId));
    }

    @PatchMapping("/{topic-id}")
    public ResponseEntity<TopicDetailVO> updateTopic(
            @PathVariable("topic-id") Long topicId,
            @RequestBody UpdateTopicDTO updateDTO
    ) {
        log.info("Update topic id: {}, data: {}", topicId, updateDTO);
        return ResponseEntity.ok(topicDetailService.updateTopic(topicId, updateDTO));
    }

    @DeleteMapping("/{topic-id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topic-id") Long topicId) {
        log.info("Delete topic id: {}", topicId);
        topicDetailService.deleteTopic(topicId);
        return ResponseEntity.noContent().build();
    }

}
