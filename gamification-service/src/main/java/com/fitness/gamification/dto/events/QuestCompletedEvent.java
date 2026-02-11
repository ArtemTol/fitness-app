package com.fitness.gamification.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestCompletedEvent {
    private Long userId;
    private Long questId;
    private String questTitle;
    private String questType;
    private Integer expReward;
    private Integer progress;
    private Integer goal;
    private LocalDateTime completedAt;
}