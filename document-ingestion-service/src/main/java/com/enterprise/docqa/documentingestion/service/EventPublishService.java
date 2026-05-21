package com.enterprise.docqa.documentingestion.service;

import com.enterprise.docqa.common.KafkaTopics;
import com.enterprise.docqa.documentingestion.model.DocumentEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventPublishService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventPublishService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDocumentUploaded(DocumentEntity documentEntity) {
        var event = Map.<String, Object>of(
                "documentId", documentEntity.getId(),
                "filename", documentEntity.getFilename(),
                "storagePath", documentEntity.getStoragePath(),
                "uploadedAt", documentEntity.getUploadedAt().toString()
        );
        kafkaTemplate.send(KafkaTopics.DOCUMENT_UPLOADED, documentEntity.getId(), event);
    }
}
