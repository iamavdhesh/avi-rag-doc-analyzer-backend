package com.enterprise.docqa.aiorchestrator.controller;

import com.enterprise.docqa.aiorchestrator.dto.ChatRequest;
import com.enterprise.docqa.aiorchestrator.service.AIOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class AIController {

    private final AIOrchestrationService orchestrationService;

    public AIController(AIOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/query")
    public Mono<ResponseEntity<Object>> query(@RequestBody ChatRequest request) {
        return orchestrationService.askQuestion(request)
                .map(resp -> ResponseEntity.ok().body((Object) resp))
                .onErrorResume(error -> {
                    Map<String, Object> errorBody = new HashMap<>();
                    errorBody.put("error", error.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body((Object) errorBody));
                });
    }
}
