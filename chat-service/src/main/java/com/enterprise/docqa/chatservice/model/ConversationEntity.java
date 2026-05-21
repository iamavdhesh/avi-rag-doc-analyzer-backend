package com.enterprise.docqa.chatservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "chat_history")
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chat_messages", joinColumns = @JoinColumn(name = "conversation_id"))
    private List<ChatMessageEntity> messages;

    @Column(nullable = false)
    private Instant lastUpdated;

    public ConversationEntity() {}

    public ConversationEntity(String userId, String title, List<ChatMessageEntity> messages, Instant lastUpdated) {
        this.userId = userId;
        this.title = title;
        this.messages = messages;
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public List<ChatMessageEntity> getMessages() {
        return messages;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }
}
