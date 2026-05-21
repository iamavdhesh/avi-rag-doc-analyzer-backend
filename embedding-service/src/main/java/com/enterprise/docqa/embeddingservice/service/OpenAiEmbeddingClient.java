package com.enterprise.docqa.embeddingservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OpenAiEmbeddingClient {

    private final WebClient webClient;
    private final String model;

    public OpenAiEmbeddingClient(@Value("${openai.api-key}") String apiKey,
                                 @Value("${openai.embeddings-model}") String model) {
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<List<Float>> createEmbedding(String text) {
        return webClient.post()
                .uri("/embeddings")
                .bodyValue(new OpenAiEmbeddingRequest(model, text))
                .retrieve()
                .bodyToMono(OpenAiEmbeddingResponse.class)
                .map(response -> response.data().get(0).embedding());
    }

    private record OpenAiEmbeddingRequest(String model, String input) {}

    private record OpenAiEmbeddingResponse(List<EmbeddingData> data) {
    }

    private record EmbeddingData(List<Float> embedding) {}
}
