package com.enterprise.docqa.vectorsearch.controller;

import com.enterprise.docqa.vectorsearch.model.DocumentChunkEntity;
import com.enterprise.docqa.vectorsearch.service.VectorSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class VectorSearchController {

    private final VectorSearchService searchService;

    public VectorSearchController(VectorSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/relevant")
    public ResponseEntity<List<DocumentChunkEntity>> search(@RequestParam("queryEmbedding") List<Float> queryEmbedding,
                                                             @RequestParam(defaultValue = "5") int limit) {
        float[] vector = new float[queryEmbedding.size()];
        for (int i = 0; i < queryEmbedding.size(); i++) {
            vector[i] = queryEmbedding.get(i);
        }
        var result = searchService.searchSimilarChunks(vector, limit);
        return ResponseEntity.ok(result);
    }
}
