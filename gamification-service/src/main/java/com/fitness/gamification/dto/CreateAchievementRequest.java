package com.fitness.gamification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAchievementRequest {
    @NotBlank(message = "Achievement name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String description;
    private String iconUrl;

    @NotNull(message = "Experience reward is required")
    private Integer expReward;

    @NotBlank(message = "Condition is required")
    private String condition;

    private Boolean isActive = true;
}