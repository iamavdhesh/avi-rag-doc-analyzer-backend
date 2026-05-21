package com.enterprise.docqa.aiorchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

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
                .exchangeToMono(response -> handleEmbeddingResponse(response))
                .map(response -> response.data().get(0).embedding())
                .retryWhen(Retry.from(companion -> companion.flatMap(rs -> {
                    Throwable failure = rs.failure();
                    // If server signaled retry-after, honor that
                    if (failure instanceof RateLimitException) {
                        long wait = ((RateLimitException) failure).retryAfterSeconds;
                        return Mono.delay(Duration.ofSeconds(wait));
                    }
                    // exponential backoff (1,2,4,8,...) capped at 30s, up to 5 retries
                    if (rs.totalRetriesInARow() >= 5) {
                        return Mono.error(failure);
                    }
                    long delaySec = Math.min(1L << rs.totalRetriesInARow(), 30L);
                    return Mono.delay(Duration.ofSeconds(delaySec));
                })));
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

    private static final class RateLimitException extends RuntimeException {
        final long retryAfterSeconds;

        RateLimitException(long retryAfterSeconds, String message) {
            super(message);
            this.retryAfterSeconds = retryAfterSeconds;
        }
    }

    private Mono<EmbeddingResponse> handleEmbeddingResponse(ClientResponse response) {
        if (response.statusCode().value() == 429) {
            // read Retry-After header if present
            String ra = response.headers().header("Retry-After").stream().findFirst().orElse(null);
            var ref = new Object() {
                long wait = 1L;
            };
            if (ra != null) {
                try {
                    ref.wait = Long.parseLong(ra);
                } catch (NumberFormatException ignored) {
                }
            }
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.<EmbeddingResponse>error(new RateLimitException(ref.wait, "rate limited: " + body)));
        }

        if (response.statusCode().isError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new RuntimeException("OpenAI error: " + response.statusCode() + " - " + body)));
        }

        return response.bodyToMono(EmbeddingResponse.class);
    }
}
