package com.fitness.core.dto;

import com.fitness.core.model.Exercise;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateExerciseRequest {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String description;

    private Exercise.ExerciseType type;
    private String imageUrl;
    private Exercise.MuscleGroup muscleGroup;
    private Boolean requiresEquipment;
}