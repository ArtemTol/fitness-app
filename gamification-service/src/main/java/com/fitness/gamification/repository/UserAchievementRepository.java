package com.fitness.gamification.repository;

import com.fitness.gamification.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserId(Long userId);
    Optional<UserAchievement> findByUserIdAndAchievementId(Long userId, Long achievementId);
    List<UserAchievement> findByUserIdAndIsEarnedTrue(Long userId);
    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
}