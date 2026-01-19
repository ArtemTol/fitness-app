package com.fitness.gamification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAchievementDTO {
    private Long id;
    private Long userId;
    private AchievementDTO achievement;
    private Integer progress;
    private Boolean isEarned;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime earnedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignedAt;
}