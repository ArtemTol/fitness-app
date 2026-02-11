package com.fitness.gamification.service;

import com.fitness.gamification.dto.AchievementDTO;
import com.fitness.gamification.dto.CreateAchievementRequest;
import com.fitness.gamification.dto.UpdateAchievementRequest;
import com.fitness.gamification.exception.ResourceNotFoundException;
import com.fitness.gamification.model.Achievement;
import com.fitness.gamification.model.UserAchievement;
import com.fitness.gamification.repository.AchievementRepository;
import com.fitness.gamification.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final GamificationEventProducer eventProducer;

    public List<AchievementDTO> getAllAchievements(Boolean activeOnly) {
        log.info("Getting all achievements, activeOnly: {}", activeOnly);

        List<Achievement> achievements;
        if (activeOnly != null && activeOnly) {
            achievements = achievementRepository.findByIsActiveTrue();
        } else {
            achievements = achievementRepository.findAll();
        }

        return achievements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AchievementDTO getAchievementById(Long id) {
        log.info("Getting achievement by id: {}", id);
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));
        return convertToDTO(achievement);
    }

    @Transactional
    public AchievementDTO createAchievement(CreateAchievementRequest request) {
        log.info("Creating new achievement: {}", request.getName());

        Achievement achievement = new Achievement();
        achievement.setName(request.getName());
        achievement.setDescription(request.getDescription());
        achievement.setIconUrl(request.getIconUrl());
        achievement.setExpReward(request.getExpReward());
        achievement.setCondition(request.getCondition());
        achievement.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Achievement savedAchievement = achievementRepository.save(achievement);
        log.info("Achievement created successfully with id: {}", savedAchievement.getId());

        return convertToDTO(savedAchievement);
    }

    @Transactional
    public AchievementDTO updateAchievement(Long id, UpdateAchievementRequest request) {
        log.info("Updating achievement with id: {}", id);

        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));

        if (request.getName() != null) {
            achievement.setName(request.getName());
        }
        if (request.getDescription() != null) {
            achievement.setDescription(request.getDescription());
        }
        if (request.getIconUrl() != null) {
            achievement.setIconUrl(request.getIconUrl());
        }
        if (request.getExpReward() != null) {
            achievement.setExpReward(request.getExpReward());
        }
        if (request.getCondition() != null) {
            achievement.setCondition(request.getCondition());
        }
        if (request.getIsActive() != null) {
            achievement.setIsActive(request.getIsActive());
        }

        Achievement updatedAchievement = achievementRepository.save(achievement);
        log.info("Achievement updated successfully with id: {}", id);

        return convertToDTO(updatedAchievement);
    }

    @Transactional
    public void deleteAchievement(Long id) {
        log.info("Deleting achievement with id: {}", id);

        if (!achievementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Achievement not found with id: " + id);
        }

        achievementRepository.deleteById(id);
        log.info("Achievement deleted successfully with id: {}", id);
    }

    // ============= МЕТОДЫ ДЛЯ ГЕЙМИФИКАЦИИ =============

    @Transactional
    public void grantWelcomeAchievement(Long userId) {
        Achievement welcome = achievementRepository.findByCondition("WELCOME")
                .orElseGet(() -> {
                    Achievement achievement = new Achievement();
                    achievement.setName("Добро пожаловать!");
                    achievement.setDescription("Начни свой путь в фитнесе");
                    achievement.setExpReward(50);
                    achievement.setCondition("WELCOME");  // ✅ condition, а не conditionType
                    achievement.setIsActive(true);
                    achievement.setIconUrl("/achievements/welcome.png");
                    return achievementRepository.save(achievement);
                });

        // Проверяем, не выдавали ли уже
        if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, welcome.getId())) {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUserId(userId);
            userAchievement.setAchievement(welcome);
            userAchievement.setEarnedAt(LocalDateTime.now());
            userAchievement.setProgress(1);
            userAchievement.setIsEarned(true);

            userAchievementRepository.save(userAchievement);

            // 🚀 ОТПРАВЛЯЕМ СОБЫТИЕ - Достижение получено!
            eventProducer.publishAchievementEarned(
                    userId,
                    welcome.getCondition(),
                    welcome.getName(),
                    welcome.getDescription(),
                    welcome.getIconUrl(),
                    welcome.getExpReward()
            );

            log.info("🏅 Приветственное достижение выдано пользователю {}", userId);
        }
    }

    @Transactional
    public void checkAndAward(Long userId, String condition, String name) {
        Achievement achievement = achievementRepository.findByCondition(condition)
                .orElseGet(() -> {
                    Achievement newAchievement = new Achievement();
                    newAchievement.setName(name);
                    newAchievement.setDescription("Достижение за " + name);
                    newAchievement.setExpReward(100);
                    newAchievement.setCondition(condition);
                    newAchievement.setIsActive(true);
                    return achievementRepository.save(newAchievement);
                });

        if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId())) {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUserId(userId);
            userAchievement.setAchievement(achievement);
            userAchievement.setEarnedAt(LocalDateTime.now());
            userAchievement.setProgress(1);
            userAchievement.setIsEarned(true);

            userAchievementRepository.save(userAchievement);

            // 🚀 ОТПРАВЛЯЕМ СОБЫТИЕ - Достижение получено!
            eventProducer.publishAchievementEarned(
                    userId,
                    achievement.getCondition(),
                    achievement.getName(),
                    achievement.getDescription(),
                    achievement.getIconUrl(),
                    achievement.getExpReward()
            );

            log.info("🏅 Достижение '{}' выдано пользователю {}", name, userId);
        }
    }

    public void checkWorkoutAchievements(Long userId, int duration) {
        // TODO: Подсчитать количество тренировок пользователя
        long workoutCount = 1; // Временно

        if (workoutCount == 1) {
            checkAndAward(userId, "FIRST_WORKOUT", "Первая тренировка");
        }
        if (duration >= 60) {
            checkAndAward(userId, "WORKOUT_60MIN", "Часовая тренировка");
        }
    }

    private AchievementDTO convertToDTO(Achievement achievement) {
        AchievementDTO dto = new AchievementDTO();
        dto.setId(achievement.getId());
        dto.setName(achievement.getName());
        dto.setDescription(achievement.getDescription());
        dto.setIconUrl(achievement.getIconUrl());
        dto.setExpReward(achievement.getExpReward());
        dto.setCondition(achievement.getCondition());
        dto.setIsActive(achievement.getIsActive());
        dto.setCreatedAt(achievement.getCreatedAt());
        dto.setUpdatedAt(achievement.getUpdatedAt());
        return dto;
    }
}