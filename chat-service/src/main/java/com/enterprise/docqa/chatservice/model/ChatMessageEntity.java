package com.enterprise.docqa.chatservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;

@Embeddable
public class ChatMessageEntity {

    @Column(nullable = false)
    private String sender;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    @Column(nullable = false)
    private Instant timestamp;

    public ChatMessageEntity() {}

    public ChatMessageEntity(String sender, String text, Instant timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
