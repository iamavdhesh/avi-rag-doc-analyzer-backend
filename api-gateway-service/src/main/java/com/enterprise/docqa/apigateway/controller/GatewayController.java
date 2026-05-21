package com.enterprise.docqa.apigateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final WebClient webClient;
    private final String authServiceUrl;
    private final String documentServiceUrl;

    public GatewayController(@Value("${services.auth}") String authServiceUrl,
                             @Value("${services.document}") String documentServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.authServiceUrl = authServiceUrl;
        this.documentServiceUrl = documentServiceUrl;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API Gateway is healthy");
    }

    @GetMapping("/documents/{id}")
    public Mono<ResponseEntity<String>> proxyDocument(@PathVariable String id) {
        return webClient.get()
                .uri(documentServiceUrl + "/api/documents/" + id)
                .retrieve()
                .toEntity(String.class);
    }
}
