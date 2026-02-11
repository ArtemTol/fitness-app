package com.fitness.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FitnessEvent {
    private String id;
    private String type;        // "EXERCISE_CREATED", "USER_REGISTERED" и т.д.
    private Object data;        // ExerciseDTO, UserDTO и т.д.
    private String source;      // "core-service", "gamification-service" и т.д.
    private LocalDateTime timestamp;
    private String userId;      // Для кого событие (если есть)

    public FitnessEvent(String type, Object data, String source) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.data = data;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }
}