package com.fitness.core.event;

public enum EventType {
    // Core Service Events
    EXERCISE_CREATED,
    EXERCISE_UPDATED,
    EXERCISE_DELETED,
    USER_REGISTERED,
    WORKOUT_COMPLETED,
    STREAK_UPDATED,
    LEVEL_UP,

    // Gamification Events
    ACHIEVEMENT_EARNED,
    QUEST_PROGRESS_UPDATED,
    QUEST_COMPLETED
}