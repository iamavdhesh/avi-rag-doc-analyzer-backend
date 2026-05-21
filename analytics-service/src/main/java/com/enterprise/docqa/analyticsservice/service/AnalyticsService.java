package com.enterprise.docqa.analyticsservice.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class AnalyticsService {

    public Map<String, Object> getStats() {
        return Map.of(
                "aiResponseLatencyMs", 375,
                "tokenConsumptionPerHour", 2048,
                "activeConversations", 14,
                "lastUpdated", Instant.now().toString()
        );
    }

    public Map<String, Object> getKafkaStatus() {
        return Map.of(
                "document-uploaded", Map.of("partitions", 6, "lag", 18, "status", "Healthy"),
                "embedding-generated", Map.of("partitions", 4, "lag", 2, "status", "Healthy"),
                "chat-requested", Map.of("partitions", 3, "lag", 9, "status", "Degraded")
        );
    }
}
