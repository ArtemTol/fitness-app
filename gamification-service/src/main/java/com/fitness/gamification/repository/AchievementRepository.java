package com.fitness.gamification.repository;

import com.fitness.gamification.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByIsActiveTrue();
    List<Achievement> findByConditionContaining(String condition);
}