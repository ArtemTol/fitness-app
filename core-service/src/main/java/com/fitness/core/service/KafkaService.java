package com.fitness.core.service;

import com.fitness.core.dto.ExerciseDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaService {

    private final KafkaTemplate<String, ExerciseDTO> kafkaTemplate;

    @Value("${spring.kafka.topic-client}")
    String clientTopic;

    /**
     * не забыть выставить потребителям разные consumer groups!
     * Каждый сервис в своей группе - все получают все сообщения!
     *
     */

    public void sendKafkaMessage(ExerciseDTO body) {
        kafkaTemplate.send(clientTopic, body);
    }
}
