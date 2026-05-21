package com.enterprise.docqa.aiorchestrator.controller;

import com.enterprise.docqa.aiorchestrator.dto.ChatRequest;
import com.enterprise.docqa.aiorchestrator.service.AIOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
public class AIController {

    private final AIOrchestrationService orchestrationService;

    public AIController(AIOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/query")
    public Mono<ResponseEntity<?>> query(@RequestBody ChatRequest request) {
        return orchestrationService.askQuestion(request)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.internalServerError().body(error.getMessage())));
    }
}
