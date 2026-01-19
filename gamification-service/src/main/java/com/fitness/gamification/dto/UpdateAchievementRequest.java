package com.fitness.gamification.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAchievementRequest {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String description;
    private String iconUrl;
    private Integer expReward;
    private String condition;
    private Boolean isActive;
}