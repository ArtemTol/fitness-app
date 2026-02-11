package com.fitness.gamification.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestProgressEvent {
    private Long userId;
    private Long questId;
    private String questTitle;
    private Integer currentProgress;
    private Integer goal;
    private Integer percentComplete;
    private LocalDateTime updatedAt;
}