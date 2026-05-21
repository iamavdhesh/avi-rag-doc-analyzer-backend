package com.enterprise.docqa.eventprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaEventProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaEventProcessorApplication.class, args);
    }
}
