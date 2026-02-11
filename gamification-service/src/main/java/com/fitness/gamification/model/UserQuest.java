package com.fitness.gamification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_quests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @Column(name = "progress", nullable = false)
    private Integer progress = 0;

    @Column(nullable = false)
    private Integer goal;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "claimed_reward", nullable = false)
    private Boolean claimedReward = false;

    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}