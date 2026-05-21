package com.enterprise.docqa.aiorchestrator.service;

import com.enterprise.docqa.aiorchestrator.dto.ChatRequest;
import com.enterprise.docqa.aiorchestrator.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIOrchestrationService {

    private final OpenAIClient openAIClient;
    private final WebClient vectorSearchClient;
    private final String vectorSearchUrl;

    public AIOrchestrationService(OpenAIClient openAIClient,
                                  @Value("${vector-search.url}") String vectorSearchUrl) {
        this.openAIClient = openAIClient;
        this.vectorSearchUrl = vectorSearchUrl;
        this.vectorSearchClient = WebClient.create(vectorSearchUrl);
    }

    public Mono<ChatResponse> askQuestion(ChatRequest request) {
        var start = Instant.now();

        return openAIClient.createEmbedding(request.question())
                .flatMap(embedding -> fetchRelevantChunks(embedding, 5)
                        .flatMap(chunks -> buildPrompt(request.question(), chunks))
                )
                .flatMap(prompt -> openAIClient.generateCompletion(prompt))
                .map(answer -> new ChatResponse(answer, "vector-search+rAG", Duration.between(start, Instant.now()).toMillis()));
    }

    private Mono<List<String>> fetchRelevantChunks(List<Float> embedding, int limit) {
        return vectorSearchClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/search/relevant")
                        .queryParam("limit", limit)
                        .queryParam("queryEmbedding", embedding)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .map(chunk -> chunk.get("content").toString())
                .collectList();
    }

    private Mono<String> buildPrompt(String question, List<String> chunks) {
        var context = chunks.stream().collect(Collectors.joining("\n\n"));
        var prompt = "You are an enterprise AI assistant.\nAnswer ONLY using provided context.\n\nContext:\n" + context + "\n\nQuestion:\n" + question;
        return Mono.just(prompt);
    }
}
