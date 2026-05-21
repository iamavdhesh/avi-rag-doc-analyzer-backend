package com.enterprise.docqa.aiorchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class OpenAIClient {

    private final WebClient webClient;
    private final String embeddingsModel;
    private final String completionModel;

    public OpenAIClient(@Value("${openai.api-key}") String apiKey,
                        @Value("${openai.embeddings-model}") String embeddingsModel,
                        @Value("${openai.completion-model}") String completionModel) {
        this.embeddingsModel = embeddingsModel;
        this.completionModel = completionModel;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<List<Float>> createEmbedding(String text) {
        return webClient.post()
                .uri("/embeddings")
                .bodyValue(Map.of("model", embeddingsModel, "input", text))
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .map(response -> response.data().get(0).embedding());
    }

    public Mono<String> generateCompletion(String prompt) {
        return webClient.post()
                .uri("/responses")
                .bodyValue(Map.of(
                        "model", completionModel,
                        "input", prompt
                ))
                .retrieve()
                .bodyToMono(CompletionResponse.class)
                .map(response -> response.output().get(0).content().get(0).text());
    }

    private record EmbeddingResponse(List<EmbeddingData> data) {}
    private record EmbeddingData(List<Float> embedding) {}
    private record CompletionResponse(List<CompletionOutput> output) {}
    private record CompletionOutput(List<CompletionContent> content) {}
    private record CompletionContent(String text) {}
}
