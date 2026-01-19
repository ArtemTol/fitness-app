package com.fitness.gamification.repository;

import com.fitness.gamification.model.UserQuest;
import com.fitness.gamification.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestRepository extends JpaRepository<UserQuest, Long> {
    List<UserQuest> findByUserId(Long userId);
    Optional<UserQuest> findByUserIdAndQuestId(Long userId, Long questId);
    List<UserQuest> findByUserIdAndIsCompletedTrue(Long userId);
    List<UserQuest> findByUserIdAndQuest_TypeAndIsCompletedFalse(Long userId, Quest.QuestType type);
}