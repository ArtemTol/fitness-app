package com.fitness.gamification.dto;

import com.fitness.gamification.model.Quest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateQuestRequest {
    private String title;
    private String description;
    private Quest.QuestType type;
    private Integer rewardExp;
    private Integer goal;
    private String conditionType;
    private Boolean isActive;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
}