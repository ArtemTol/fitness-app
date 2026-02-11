package com.fitness.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutCompletedEvent {
    private Long userId;
    private Integer durationMinutes;
    private LocalDateTime completedAt;

    public WorkoutCompletedEvent(Long userId, Integer durationMinutes) {
        this.userId = userId;
        this.durationMinutes = durationMinutes;
        this.completedAt = LocalDateTime.now();
    }
}