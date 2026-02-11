package com.fitness.gamification.service;

import com.fitness.gamification.dto.CreateQuestRequest;
import com.fitness.gamification.dto.QuestDTO;
import com.fitness.gamification.dto.UpdateQuestRequest;
import com.fitness.gamification.exception.ResourceNotFoundException;
import com.fitness.gamification.model.Quest;
import com.fitness.gamification.repository.QuestRepository;
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
public class QuestService {

    private final QuestRepository questRepository;

    public List<QuestDTO> getAllQuests(Boolean activeOnly) {
        List<Quest> quests = activeOnly != null && activeOnly
                ? questRepository.findByIsActiveTrue()
                : questRepository.findAll();

        return quests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuestDTO getQuestById(Long id) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest not found with id: " + id));
        return convertToDTO(quest);
    }

    public List<QuestDTO> getAvailableQuests() {
        LocalDateTime now = LocalDateTime.now();
        return questRepository
                .findByIsActiveTrueAndAvailableFromBeforeAndAvailableToAfter(now, now)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestDTO createQuest(CreateQuestRequest request) {
        Quest quest = new Quest();
        quest.setTitle(request.getTitle());
        quest.setDescription(request.getDescription());
        quest.setType(request.getType());
        quest.setRewardExp(request.getRewardExp());
        quest.setGoal(request.getGoal());
        quest.setConditionType(request.getConditionType());
        quest.setAvailableFrom(request.getAvailableFrom());
        quest.setAvailableTo(request.getAvailableTo());
        quest.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Quest savedQuest = questRepository.save(quest);
        log.info("✅ Квест создан: {}", savedQuest.getId());
        return convertToDTO(savedQuest);
    }

    @Transactional
    public QuestDTO updateQuest(Long id, UpdateQuestRequest request) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest not found with id: " + id));

        if (request.getTitle() != null) quest.setTitle(request.getTitle());
        if (request.getDescription() != null) quest.setDescription(request.getDescription());
        if (request.getRewardExp() != null) quest.setRewardExp(request.getRewardExp());
        if (request.getGoal() != null) quest.setGoal(request.getGoal());
        if (request.getIsActive() != null) quest.setIsActive(request.getIsActive());
        if (request.getAvailableFrom() != null) quest.setAvailableFrom(request.getAvailableFrom());
        if (request.getAvailableTo() != null) quest.setAvailableTo(request.getAvailableTo());

        return convertToDTO(questRepository.save(quest));
    }

    @Transactional
    public void deleteQuest(Long id) {
        if (!questRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quest not found with id: " + id);
        }
        questRepository.deleteById(id);
        log.info("✅ Квест удален: {}", id);
    }

    // 👇 ДОБАВЛЯЕМ ЭТИ МЕТОДЫ!

    public void assignDailyQuests(Long userId) {
        log.info("📋 Назначение daily квестов пользователю {}", userId);
        // TODO: Реализовать логику назначения daily квестов
    }

    public void assignWeeklyQuests(Long userId) {
        log.info("📋 Назначение weekly квестов пользователю {}", userId);
        // TODO: Реализовать логику назначения weekly квестов
    }

    public void updateQuestProgress(Long userId, String conditionType, int increment) {
        log.info("📊 Обновление прогресса квеста: user={}, type={}, +{}",
                userId, conditionType, increment);
        // TODO: Реализовать логику обновления прогресса квестов
    }

    private QuestDTO convertToDTO(Quest quest) {
        QuestDTO dto = new QuestDTO();
        dto.setId(quest.getId());
        dto.setTitle(quest.getTitle());
        dto.setDescription(quest.getDescription());
        dto.setType(quest.getType());
        dto.setRewardExp(quest.getRewardExp());
        dto.setIsActive(quest.getIsActive());
        dto.setGoal(quest.getGoal());
        dto.setConditionType(quest.getConditionType());
        dto.setAvailableFrom(quest.getAvailableFrom());
        dto.setAvailableTo(quest.getAvailableTo());
        dto.setCreatedAt(quest.getCreatedAt());
        dto.setUpdatedAt(quest.getUpdatedAt());
        return dto;
    }
}