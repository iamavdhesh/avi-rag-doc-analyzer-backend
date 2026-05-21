package com.enterprise.docqa.vectorsearch.repository;

import com.enterprise.docqa.vectorsearch.model.DocumentChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunkEntity, String> {

    @Query(value = "SELECT *, embedding <#> CAST(:vector AS vector) AS distance FROM document_chunks ORDER BY distance LIMIT :limit", nativeQuery = true)
    List<DocumentChunkEntity> findMostRelevant(@Param("vector") float[] vector, @Param("limit") int limit);
}
