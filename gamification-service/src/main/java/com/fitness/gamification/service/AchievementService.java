package com.fitness.gamification.service;

import com.fitness.gamification.dto.AchievementDTO;
import com.fitness.gamification.dto.CreateAchievementRequest;
import com.fitness.gamification.dto.UpdateAchievementRequest;
import com.fitness.gamification.exception.ResourceNotFoundException;
import com.fitness.gamification.model.Achievement;
import com.fitness.gamification.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;

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