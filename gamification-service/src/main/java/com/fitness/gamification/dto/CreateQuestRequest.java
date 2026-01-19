package com.fitness.gamification.dto;

import com.fitness.gamification.model.Quest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateQuestRequest {
    @NotBlank
    private String title;
    private String description;

    @NotNull
    private Quest.QuestType type;

    @NotNull
    private Integer rewardExp;

    @NotNull
    private Integer goal;

    @NotBlank
    private String conditionType;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Boolean isActive = true;
}