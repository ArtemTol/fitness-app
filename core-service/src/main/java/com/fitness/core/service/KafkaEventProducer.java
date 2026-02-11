package com.fitness.core.service;

import com.fitness.core.dto.ExerciseDTO;
import com.fitness.core.dto.events.StreakUpdatedEvent;
import com.fitness.core.dto.events.UserRegisteredEvent;
import com.fitness.core.dto.events.WorkoutCompletedEvent;
import com.fitness.core.event.EventType;
import com.fitness.core.event.FitnessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final KafkaTemplate<String, FitnessEvent> kafkaTemplate;

    @Value("${spring.kafka.topic-client:clients.status}")
    private String topic;

    // ============= EXERCISE EVENTS =============

    public void sendExerciseCreatedEvent(ExerciseDTO exercise) {
        FitnessEvent event = new FitnessEvent(
                EventType.EXERCISE_CREATED,
                exercise,
                "core-service"
        );

        kafkaTemplate.send(topic, event)
                .thenAccept(result -> {
                    log.info("✅ [EXERCISE_CREATED] Упражнение {} отправлено", exercise.getId());
                })
                .exceptionally(ex -> {
                    log.error("❌ [EXERCISE_CREATED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    // 👇 ДОБАВЛЯЕМ ЭТОТ МЕТОД!
    public void sendExerciseUpdatedEvent(ExerciseDTO exercise) {
        FitnessEvent event = new FitnessEvent(
                EventType.EXERCISE_UPDATED,
                exercise,
                "core-service"
        );

        kafkaTemplate.send(topic, event)
                .thenAccept(result -> {
                    log.info("✅ [EXERCISE_UPDATED] Упражнение {} обновлено", exercise.getId());
                })
                .exceptionally(ex -> {
                    log.error("❌ [EXERCISE_UPDATED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    // 👇 ДОБАВЛЯЕМ ЭТОТ МЕТОД!
    public void sendExerciseDeletedEvent(Long exerciseId) {
        FitnessEvent event = new FitnessEvent(
                EventType.EXERCISE_DELETED,
                exerciseId,
                "core-service"
        );

        kafkaTemplate.send(topic, event)
                .thenAccept(result -> {
                    log.info("✅ [EXERCISE_DELETED] Упражнение {} удалено", exerciseId);
                })
                .exceptionally(ex -> {
                    log.error("❌ [EXERCISE_DELETED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    // ============= USER EVENTS =============

    public void sendUserRegisteredEvent(Long userId, String username, String email) {
        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);
        FitnessEvent fitnessEvent = new FitnessEvent(
                EventType.USER_REGISTERED,
                event,
                "core-service",
                userId.toString()
        );

        kafkaTemplate.send(topic, userId.toString(), fitnessEvent)
                .thenAccept(result -> {
                    log.info("✅ [USER_REGISTERED] Пользователь {} зарегистрирован", userId);
                })
                .exceptionally(ex -> {
                    log.error("❌ [USER_REGISTERED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    // ============= WORKOUT EVENTS =============

    public void sendWorkoutCompletedEvent(Long userId, Integer durationMinutes) {
        WorkoutCompletedEvent event = new WorkoutCompletedEvent(userId, durationMinutes);
        FitnessEvent fitnessEvent = new FitnessEvent(
                EventType.WORKOUT_COMPLETED,
                event,
                "core-service",
                userId.toString()
        );

        kafkaTemplate.send(topic, userId.toString(), fitnessEvent)
                .thenAccept(result -> {
                    log.info("✅ [WORKOUT_COMPLETED] Тренировка {} мин, user={}", durationMinutes, userId);
                })
                .exceptionally(ex -> {
                    log.error("❌ [WORKOUT_COMPLETED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    // ============= STREAK EVENTS =============

    public void sendStreakUpdatedEvent(Long userId, Integer currentStreak, Integer longestStreak) {
        StreakUpdatedEvent event = new StreakUpdatedEvent(userId, currentStreak, longestStreak);
        FitnessEvent fitnessEvent = new FitnessEvent(
                EventType.STREAK_UPDATED,
                event,
                "core-service",
                userId.toString()
        );

        kafkaTemplate.send(topic, userId.toString(), fitnessEvent)
                .thenAccept(result -> {
                    log.info("🔥 [STREAK_UPDATED] {} дней, user={}", currentStreak, userId);
                })
                .exceptionally(ex -> {
                    log.error("❌ [STREAK_UPDATED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }
}