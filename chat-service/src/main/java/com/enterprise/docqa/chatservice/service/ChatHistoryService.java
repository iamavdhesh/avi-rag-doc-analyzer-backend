package com.enterprise.docqa.chatservice.service;

import com.enterprise.docqa.chatservice.model.ChatMessageEntity;
import com.enterprise.docqa.chatservice.model.ConversationEntity;
import com.enterprise.docqa.chatservice.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ChatHistoryService {

    private final ConversationRepository repository;

    public ChatHistoryService(ConversationRepository repository) {
        this.repository = repository;
    }

    public ConversationEntity createConversation(String userId, String title, List<ChatMessageEntity> messages) {
        var conversation = new ConversationEntity(userId, title, messages, Instant.now());
        return repository.save(conversation);
    }

    public Optional<ConversationEntity> findById(String id) {
        return repository.findById(id);
    }

    public List<ConversationEntity> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}
