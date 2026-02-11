package com.fitness.gamification.service;

import com.fitness.gamification.dto.UserQuestDTO;
import com.fitness.gamification.exception.ResourceNotFoundException;
import com.fitness.gamification.model.Quest;
import com.fitness.gamification.model.UserQuest;
import com.fitness.gamification.repository.QuestRepository;
import com.fitness.gamification.repository.UserQuestRepository;
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
public class UserQuestService {

    private final UserQuestRepository userQuestRepository;
    private final QuestRepository questRepository;
    private final GamificationEventProducer eventProducer;  // 👈 ДОБАВЛЯЕМ!

    public List<UserQuestDTO> getUserQuests(Long userId) {
        return userQuestRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserQuestDTO> getCompletedUserQuests(Long userId) {
        return userQuestRepository.findByUserIdAndIsCompletedTrue(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserQuestDTO assignQuestToUser(Long userId, Long questId) {
        // Проверяем, не назначен ли уже квест
        userQuestRepository.findByUserIdAndQuestId(userId, questId)
                .ifPresent(uq -> {
                    throw new IllegalStateException("Quest already assigned to user");
                });

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new ResourceNotFoundException("Quest not found with id: " + questId));

        UserQuest userQuest = new UserQuest();
        userQuest.setUserId(userId);
        userQuest.setQuest(quest);
        userQuest.setProgress(0);
        userQuest.setIsCompleted(false);
        userQuest.setClaimedReward(false);
        userQuest.setGoal(quest.getGoal());
        userQuest.setAssignedAt(LocalDateTime.now());

        UserQuest saved = userQuestRepository.save(userQuest);
        log.info("✅ Квест '{}' назначен пользователю {}", quest.getTitle(), userId);

        return convertToDTO(saved);
    }

    @Transactional
    public UserQuestDTO updateQuestProgress(Long userId, Long questId, Integer progress) {
        UserQuest userQuest = userQuestRepository
                .findByUserIdAndQuestId(userId, questId)
                .orElseThrow(() -> new ResourceNotFoundException("User quest not found"));

        userQuest.setProgress(progress);
        userQuest.setUpdatedAt(LocalDateTime.now());

        // 🚀 1. ОТПРАВЛЯЕМ СОБЫТИЕ - Прогресс обновлен!
        Quest quest = userQuest.getQuest();
        eventProducer.publishQuestProgress(
                userId,
                questId,
                quest.getTitle(),
                progress,
                quest.getGoal()
        );

        // Проверяем выполнение квеста
        if (!userQuest.getIsCompleted() && progress >= quest.getGoal()) {
            userQuest.setIsCompleted(true);
            userQuest.setCompletedAt(LocalDateTime.now());
            log.info("🎯 Квест '{}' выполнен пользователем {}!", quest.getTitle(), userId);

            // 🚀 2. ОТПРАВЛЯЕМ СОБЫТИЕ - Квест выполнен!
            eventProducer.publishQuestCompleted(
                    userId,
                    questId,
                    quest.getTitle(),
                    quest.getType().toString(),
                    quest.getRewardExp(),
                    progress,
                    quest.getGoal()
            );
        }

        return convertToDTO(userQuestRepository.save(userQuest));
    }

    @Transactional
    public UserQuestDTO claimQuestReward(Long userId, Long questId) {
        UserQuest userQuest = userQuestRepository
                .findByUserIdAndQuestId(userId, questId)
                .orElseThrow(() -> new ResourceNotFoundException("User quest not found"));

        if (!userQuest.getIsCompleted()) {
            throw new IllegalStateException("Quest not completed yet");
        }

        if (userQuest.getClaimedReward()) {
            throw new IllegalStateException("Reward already claimed");
        }

        userQuest.setClaimedReward(true);
        userQuestRepository.save(userQuest);

        log.info("🎁 Награда за квест '{}' получена пользователем {}",
                userQuest.getQuest().getTitle(), userId);

        return convertToDTO(userQuest);
    }

    private UserQuestDTO convertToDTO(UserQuest userQuest) {
        UserQuestDTO dto = new UserQuestDTO();
        dto.setId(userQuest.getId());
        dto.setUserId(userQuest.getUserId());
        dto.setQuestId(userQuest.getQuest().getId());
        dto.setProgress(userQuest.getProgress());
        dto.setIsCompleted(userQuest.getIsCompleted());
        dto.setClaimedReward(userQuest.getClaimedReward());
        dto.setCompletedAt(userQuest.getCompletedAt());
        dto.setAssignedAt(userQuest.getAssignedAt());
        dto.setUpdatedAt(userQuest.getUpdatedAt());
        return dto;
    }
}