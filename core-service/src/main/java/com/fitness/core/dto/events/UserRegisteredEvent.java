package com.fitness.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private Long userId;
    private String username;
    private String email;
    private LocalDateTime registeredAt;

    public UserRegisteredEvent(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.registeredAt = LocalDateTime.now();
    }
}