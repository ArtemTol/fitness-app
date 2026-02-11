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
    private EventType type;
    private Object payload;
    private String source;
    private String userId;
    private LocalDateTime timestamp;

    public FitnessEvent(EventType type, Object payload, String source) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.payload = payload;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    public FitnessEvent(EventType type, Object payload, String source, String userId) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.payload = payload;
        this.source = source;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
}