package com.fitness.core.dto;

import com.fitness.core.model.Exercise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ExerciseDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Exercise name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Exercise type is required")
    private Exercise.ExerciseType type;

    private String imageUrl;

    @NotNull(message = "Muscle group is required")
    private Exercise.MuscleGroup muscleGroup;

    @NotNull(message = "Equipment requirement must be specified")
    private Boolean requiresEquipment;

    //TODO статус тренировки
}
