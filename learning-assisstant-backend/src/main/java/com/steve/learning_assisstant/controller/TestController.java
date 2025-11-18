package com.steve.learning_assisstant.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class TestController {

    private final ChatClient chatClient;

    public TestController (@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/test-chat")
    public ResponseEntity<String> testChat(
            @RequestParam("conversationId") String conversationId,
            @RequestParam("message") String message
    ) {
        return ResponseEntity.ok(
                chatClient
                        .prompt()
                        .user(message)
                        .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId))
                        .call()
                        .content()
        );
    }
}
