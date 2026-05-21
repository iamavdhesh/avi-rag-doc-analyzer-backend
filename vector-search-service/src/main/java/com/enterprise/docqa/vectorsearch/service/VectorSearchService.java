package com.enterprise.docqa.vectorsearch.service;

import com.enterprise.docqa.vectorsearch.model.DocumentChunkEntity;
import com.enterprise.docqa.vectorsearch.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorSearchService {

    private final DocumentChunkRepository repository;

    public VectorSearchService(DocumentChunkRepository repository) {
        this.repository = repository;
    }

    public List<DocumentChunkEntity> searchSimilarChunks(float[] queryEmbedding, int limit) {
        return repository.findMostRelevant(queryEmbedding, limit);
    }
}
