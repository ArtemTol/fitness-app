package com.fitness.gamification.repository;

import com.fitness.gamification.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findByIsActiveTrue();


    Optional<Achievement> findByCondition(String condition);
    // Optional<Achievement> findByConditionType(String conditionType);
}