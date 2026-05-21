package com.enterprise.docqa.analyticsservice.controller;

import com.enterprise.docqa.analyticsservice.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(analyticsService.getStats());
    }

    @GetMapping("/kafka/status")
    public ResponseEntity<?> kafkaStatus() {
        return ResponseEntity.ok(analyticsService.getKafkaStatus());
    }
}
