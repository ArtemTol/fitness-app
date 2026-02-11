package com.fitness.gamification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.core.dto.ExerciseDTO;
import com.fitness.core.event.EventType;
import com.fitness.core.event.FitnessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FitnessEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topic-client:clients.status}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleEvent(FitnessEvent event) {
        log.info("📨 ПОЛУЧЕНО СОБЫТИЕ: type={}, source={}, id={}",
                event.getType(), event.getSource(), event.getId());

        // ✅ ИСПРАВЛЕНО: event.getType() это EventType, сравниваем с Enum
        if (event.getType() == EventType.EXERCISE_CREATED) {
            handleExerciseCreated(event);
        } else {
            log.warn("Неизвестный тип события: {}", event.getType());
        }
    }

    private void handleExerciseCreated(FitnessEvent event) {
        try {
            // ✅ ИСПРАВЛЕНО: getPayload() вместо getData()!
            ExerciseDTO exercise = objectMapper.convertValue(event.getPayload(), ExerciseDTO.class);
            log.info("🏋️ УПРАЖНЕНИЕ СОЗДАНО: id={}, name={}",
                    exercise.getId(), exercise.getName());

            log.info("✅ Событие обработано успешно");
        } catch (Exception e) {
            log.error("❌ Ошибка обработки упражнения: {}", e.getMessage());
        }
    }
}