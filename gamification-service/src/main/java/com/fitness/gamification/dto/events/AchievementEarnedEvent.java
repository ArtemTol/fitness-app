package com.fitness.gamification.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementEarnedEvent {
    private Long userId;
    private String achievementCode;
    private String achievementName;
    private String description;
    private String iconUrl;
    private Integer expReward;
    private LocalDateTime earnedAt;
}