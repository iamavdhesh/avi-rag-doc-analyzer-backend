package com.enterprise.docqa.documentingestion.controller;

import com.enterprise.docqa.common.KafkaTopics;
import com.enterprise.docqa.common.dto.StandardResponse;
import com.enterprise.docqa.documentingestion.model.DocumentEntity;
import com.enterprise.docqa.documentingestion.service.DocumentStorageService;
import com.enterprise.docqa.documentingestion.service.EventPublishService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentStorageService documentStorageService;
    private final EventPublishService eventPublishService;

    public DocumentController(DocumentStorageService documentStorageService, EventPublishService eventPublishService) {
        this.documentStorageService = documentStorageService;
        this.eventPublishService = eventPublishService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StandardResponse> uploadDocument(@RequestPart("file") MultipartFile file) {
        DocumentEntity saved = documentStorageService.storeFile(file);
        eventPublishService.publishDocumentUploaded(saved);
        var payload = new DocumentUploadResponse(saved.getId(), saved.getFilename(), saved.getStatus());
        return ResponseEntity.created(URI.create("/api/documents/" + saved.getId()))
                .body(StandardResponse.ok("Document uploaded successfully", payload));
    }

    private record DocumentUploadResponse(String id, String filename, String status) {}
}
