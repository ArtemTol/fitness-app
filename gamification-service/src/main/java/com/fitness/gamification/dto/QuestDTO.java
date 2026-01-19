package com.fitness.gamification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitness.gamification.model.Quest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestDTO {
    private Long id;
    private String title;
    private String description;
    private Quest.QuestType type;
    private Integer rewardExp;
    private Boolean isActive;
    private Integer goal;
    private String conditionType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime availableFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime availableTo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}