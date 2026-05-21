package com.enterprise.docqa.documentingestion.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private Instant uploadedAt;

    public DocumentEntity() {}

    public DocumentEntity(String filename, String status, String storagePath, String contentType, long size, Instant uploadedAt) {
        this.filename = filename;
        this.status = status;
        this.storagePath = storagePath;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getStatus() {
        return status;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }
}
