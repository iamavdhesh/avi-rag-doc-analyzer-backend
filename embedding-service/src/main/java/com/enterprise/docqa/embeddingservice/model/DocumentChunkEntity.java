package com.enterprise.docqa.embeddingservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "document_chunks")
public class DocumentChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String documentId;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private List<Float> embedding;

    @Column(nullable = false)
    private int chunkIndex;

    public DocumentChunkEntity() {}

    public DocumentChunkEntity(String id, String documentId, String content, List<Float> embedding, int chunkIndex) {
        this.id = id;
        this.documentId = documentId;
        this.content = content;
        this.embedding = embedding;
        this.chunkIndex = chunkIndex;
    }

    public String getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getContent() {
        return content;
    }

    public List<Float> getEmbedding() {
        return embedding;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }
}
