package com.enterprise.docqa.chatservice.controller;

import com.enterprise.docqa.chatservice.model.ConversationEntity;
import com.enterprise.docqa.chatservice.service.ChatHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    public ChatHistoryController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<ConversationEntity>> history(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(chatHistoryService.findByUserId(userId));
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<ConversationEntity> getConversation(@PathVariable String conversationId) {
        return chatHistoryService.findById(conversationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
