package com.enterprise.docqa.vectorsearch.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Immutable;

import java.util.List;

@Entity
@Table(name = "document_chunks")
@Immutable
public class DocumentChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String documentId;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(columnDefinition = "vector(1536)", nullable = false)
    private List<Float> embedding;

    @Column(nullable = false)
    private int chunkIndex;

    public DocumentChunkEntity() {}

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
