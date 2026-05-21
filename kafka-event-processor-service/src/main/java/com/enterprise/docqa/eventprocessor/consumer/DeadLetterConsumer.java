package com.enterprise.docqa.eventprocessor.consumer;

import com.enterprise.docqa.common.KafkaTopics;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DeadLetterConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.AI_FAILED_EVENTS, groupId = "event-processor-group")
    public void consumeFailedEvent(ConsumerRecord<String, Object> record) {
        // Implement idempotent retry / failure notification logic
        var eventKey = record.key();
        var payload = record.value();
        System.err.printf("DLQ event received: %s -> %s%n", eventKey, payload);
    }
}
