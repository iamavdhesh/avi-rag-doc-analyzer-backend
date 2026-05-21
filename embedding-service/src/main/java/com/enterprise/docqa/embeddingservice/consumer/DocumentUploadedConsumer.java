package com.enterprise.docqa.embeddingservice.consumer;

import com.enterprise.docqa.common.KafkaTopics;
import com.enterprise.docqa.embeddingservice.model.DocumentChunkEntity;
import com.enterprise.docqa.embeddingservice.repository.DocumentChunkRepository;
import com.enterprise.docqa.embeddingservice.service.OpenAiEmbeddingClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DocumentUploadedConsumer {

    private final OpenAiEmbeddingClient embeddingClient;
    private final DocumentChunkRepository chunkRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DocumentUploadedConsumer(OpenAiEmbeddingClient embeddingClient, DocumentChunkRepository chunkRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.embeddingClient = embeddingClient;
        this.chunkRepository = chunkRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.DOCUMENT_UPLOADED, groupId = "embedding-service-group")
    public void onDocumentUploaded(Map<String, Object> payload) {
        var documentId = payload.get("documentId").toString();
        var storagePath = payload.get("storagePath").toString();
        var chunks = chunkDocumentText("Simulated chunk extraction for " + payload.get("filename"));

        chunks.forEach(chunk -> {
            embeddingClient.createEmbedding(chunk)
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(embedding -> {
                        var entity = new DocumentChunkEntity(UUID.randomUUID().toString(), documentId, chunk, embedding, 0);
                        chunkRepository.save(entity);
                        kafkaTemplate.send(KafkaTopics.EMBEDDING_GENERATED, documentId, Map.of(
                                "chunkId", entity.getId(),
                                "documentId", documentId,
                                "status", "EMBEDDED"
                        ));
                    });
        });
    }

    private List<String> chunkDocumentText(String text) {
        return List.of(text, text + " [chunk 2]", text + " [chunk 3]");
    }
}
