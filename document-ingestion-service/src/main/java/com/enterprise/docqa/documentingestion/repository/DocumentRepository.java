package com.enterprise.docqa.documentingestion.repository;

import com.enterprise.docqa.documentingestion.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {
}
