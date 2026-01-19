package com.fitness.gamification.repository;

import com.fitness.gamification.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findByIsActiveTrue();
    List<Quest> findByTypeAndIsActiveTrue(Quest.QuestType type);
    List<Quest> findByIsActiveTrueAndAvailableFromBeforeAndAvailableToAfter(
            LocalDateTime now1, LocalDateTime now2);
}