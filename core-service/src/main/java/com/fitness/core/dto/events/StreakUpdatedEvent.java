package com.fitness.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreakUpdatedEvent {
    private Long userId;
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDateTime updatedAt;

    public StreakUpdatedEvent(Long userId, Integer currentStreak, Integer longestStreak) {
        this.userId = userId;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.updatedAt = LocalDateTime.now();
    }
}