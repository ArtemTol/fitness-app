package com.fitness.gamification.service;

import com.fitness.core.dto.ExerciseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class KafkaService {

    @KafkaListener(
            topics = "${spring.kafka.topic-client}",
            containerFactory = "stringKafkaListenerContainerFactory",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void simpleConsume(String message) {
        log.info("🎯 SIMPLE MESSAGE RECEIVED: {}", message);

        log.info("✅ Message processed successfully");
    }
}