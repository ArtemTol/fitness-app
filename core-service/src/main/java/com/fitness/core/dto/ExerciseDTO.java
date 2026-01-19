package com.fitness.core.dto;

import com.fitness.core.model.Exercise;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    //TODO статус тренировки
}
