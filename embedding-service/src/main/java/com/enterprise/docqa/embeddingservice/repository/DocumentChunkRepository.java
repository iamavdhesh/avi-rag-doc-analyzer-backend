package com.enterprise.docqa.embeddingservice.repository;

import com.enterprise.docqa.embeddingservice.model.DocumentChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunkEntity, String> {
    List<DocumentChunkEntity> findByDocumentId(String documentId);
}
