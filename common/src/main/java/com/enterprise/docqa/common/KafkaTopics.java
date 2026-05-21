package com.enterprise.docqa.common;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String DOCUMENT_UPLOADED = "document-uploaded";
    public static final String DOCUMENT_PROCESSED = "document-processed";
    public static final String EMBEDDING_GENERATED = "embedding-generated";
    public static final String CHAT_REQUESTED = "chat-requested";
    public static final String CHAT_COMPLETED = "chat-completed";
    public static final String AI_FAILED_EVENTS = "ai-failed-events";
}
