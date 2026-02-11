package com.fitness.gamification.service;

import com.fitness.core.event.EventType;
import com.fitness.core.event.FitnessEvent;
import com.fitness.gamification.dto.events.AchievementEarnedEvent;
import com.fitness.gamification.dto.events.QuestCompletedEvent;
import com.fitness.gamification.dto.events.QuestProgressEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamificationEventProducer {

    private final KafkaTemplate<String, FitnessEvent> kafkaTemplate;

    @Value("${spring.kafka.topic-client:clients.status}")
    private String topic;

    /**
     * Отправка события о получении достижения
     */
    public void publishAchievementEarned(Long userId, String achievementCode, String achievementName,
                                         String description, String iconUrl, Integer expReward) {
        AchievementEarnedEvent event = new AchievementEarnedEvent(
                userId,
                achievementCode,
                achievementName,
                description,
                iconUrl,
                expReward,
                java.time.LocalDateTime.now()
        );

        FitnessEvent fitnessEvent = new FitnessEvent(
                EventType.ACHIEVEMENT_EARNED,
                event,
                "gamification-service",
                userId.toString()
        );

        kafkaTemplate.send(topic, userId.toString(), fitnessEvent)
                .thenAccept(result -> {
                    log.info("🏅 [ACHIEVEMENT_EARNED] Пользователь {} получил: {}", userId, achievementName);
                })
                .exceptionally(ex -> {
                    log.error("❌ [ACHIEVEMENT_EARNED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    /**
     * Отправка события о прогрессе квеста
     */
    public void publishQuestProgress(Long userId, Long questId, String questTitle,
                                     Integer currentProgress, Integer goal) {
        QuestProgressEvent event = new QuestProgressEvent(
                userId,
                questId,
                questTitle,
                currentProgress,
                goal,
                (currentProgress * 100) / goal,
                java.time.LocalDateTime.now()
        );

        FitnessEvent fitnessEvent = new FitnessEvent(
                EventType.QUEST_PROGRESS_UPDATED,
                event,
                "gamification-service",
                userId.toString()
        );

        kafkaTemplate.send(topic, userId.toString(), fitnessEvent)
                .thenAccept(result -> {
                    log.debug("📊 [QUEST_PROGRESS] Квест: {} {}/{} ({}%)",
                            questTitle, currentProgress, goal, (currentProgress * 100) / goal);
                })
                .exceptionally(ex -> {
                    log.error("❌ [QUEST_PROGRESS] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }

    /**
     * Отправка события о выполнении квеста
     */
    public void publishQuestCompleted(Long userId, Long questId, String questTitle,
                                      String questType, Integer expReward, Integer progress, Integer goal) {
        QuestCompletedEvent event = new QuestCompletedEvent(
                userId,
                questId,
                questTitle,
                questType,
                expReward,
                progress,
                goal,
                java.time.LocalDateTime.now()
        );

        FitnessEvent fitnessEvent = new FitnessEvent(
                EventType.QUEST_COMPLETED,
                event,
                "gamification-service",
                userId.toString()
        );

        kafkaTemplate.send(topic, userId.toString(), fitnessEvent)
                .thenAccept(result -> {
                    log.info("✅ [QUEST_COMPLETED] Пользователь {} выполнил квест: {}", userId, questTitle);
                })
                .exceptionally(ex -> {
                    log.error("❌ [QUEST_COMPLETED] Ошибка: {}", ex.getMessage());
                    return null;
                });
    }
}