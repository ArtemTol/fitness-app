package com.fitness.gamification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.core.dto.events.StreakUpdatedEvent;
import com.fitness.core.dto.events.UserRegisteredEvent;
import com.fitness.core.dto.events.WorkoutCompletedEvent;
import com.fitness.core.event.EventType;
import com.fitness.core.event.FitnessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionConsumer {

    private final ObjectMapper objectMapper;
    private final AchievementService achievementService;
    private final QuestService questService;

    @KafkaListener(
            topics = "${spring.kafka.topic-client:clients.status}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleFitnessEvent(FitnessEvent event) {
        log.info("📨 Получен FitnessEvent: type={}, source={}, userId={}",
                event.getType(), event.getSource(), event.getUserId());

        // Только события от core-service
        if (!"core-service".equals(event.getSource())) {
            return;
        }

        try {
            // ✅ ИСПРАВЛЕНО: сравниваем с Enum, а не со String!
            if (event.getType() == EventType.USER_REGISTERED) {
                UserRegisteredEvent registered = objectMapper.convertValue(
                        event.getPayload(), UserRegisteredEvent.class);
                handleUserRegistered(registered);

            } else if (event.getType() == EventType.WORKOUT_COMPLETED) {
                WorkoutCompletedEvent workout = objectMapper.convertValue(
                        event.getPayload(), WorkoutCompletedEvent.class);
                handleWorkoutCompleted(workout);

            } else if (event.getType() == EventType.STREAK_UPDATED) {
                StreakUpdatedEvent streak = objectMapper.convertValue(
                        event.getPayload(), StreakUpdatedEvent.class);
                handleStreakUpdated(streak);

            } else {
                log.debug("Игнорируем событие: {}", event.getType());
            }
        } catch (Exception e) {
            log.error("❌ Ошибка обработки: {}", e.getMessage(), e);
        }
    }

    private void handleUserRegistered(UserRegisteredEvent event) {
        log.info("👤 Новый пользователь: id={}, username={}",
                event.getUserId(), event.getUsername());

        // 1. Приветственное достижение
        achievementService.grantWelcomeAchievement(event.getUserId());

        // 2. Daily квесты
        questService.assignDailyQuests(event.getUserId());

        // 3. Weekly квесты
        questService.assignWeeklyQuests(event.getUserId());
    }

    private void handleWorkoutCompleted(WorkoutCompletedEvent event) {
        log.info("💪 Тренировка завершена: user={}, {} мин",
                event.getUserId(), event.getDurationMinutes());

        questService.updateQuestProgress(event.getUserId(), "WORKOUT", 1);
        questService.updateQuestProgress(event.getUserId(), "WORKOUT_DURATION",
                event.getDurationMinutes());

        achievementService.checkWorkoutAchievements(event.getUserId(),
                event.getDurationMinutes());
    }

    private void handleStreakUpdated(StreakUpdatedEvent event) {
        log.info("🔥 Стрик обновлен: user={}, {} дней",
                event.getUserId(), event.getCurrentStreak());

        int streak = event.getCurrentStreak();
        if (streak == 3) {
            achievementService.checkAndAward(event.getUserId(), "STREAK_3", "3 дня подряд");
        } else if (streak == 7) {
            achievementService.checkAndAward(event.getUserId(), "STREAK_7", "7 дней подряд");
        } else if (streak == 30) {
            achievementService.checkAndAward(event.getUserId(), "STREAK_30", "30 дней подряд");
        } else if (streak == 100) {
            achievementService.checkAndAward(event.getUserId(), "STREAK_100", "100 дней подряд");
        }
    }
}