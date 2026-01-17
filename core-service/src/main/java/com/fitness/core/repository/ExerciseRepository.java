package com.fitness.core.repository;

import com.fitness.core.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMuscleGroup(Exercise.MuscleGroup muscleGroup);
    List<Exercise> findByRequiresEquipment(Boolean requiresEquipment);
}