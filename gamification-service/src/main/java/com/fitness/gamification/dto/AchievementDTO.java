package com.fitness.gamification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AchievementDTO {
    private Long id;

    @NotBlank(message = "Achievement name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String description;
    private String iconUrl;

    @NotNull(message = "Experience reward is required")
    private Integer expReward;

    @NotBlank(message = "Condition is required")
    private String condition;

    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}