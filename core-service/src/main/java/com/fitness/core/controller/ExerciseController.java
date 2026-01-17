package com.fitness.core.controller;

import com.fitness.core.dto.CreateExerciseRequest;
import com.fitness.core.dto.ExerciseDTO;
import com.fitness.core.dto.UpdateExerciseRequest;
import com.fitness.core.model.Exercise;
import com.fitness.core.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
@Tag(name = "Exercises", description = "Exercise management endpoints")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    @Operation(summary = "Get all exercises")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get exercise by ID")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    @GetMapping("/muscle-group/{muscleGroup}")
    @Operation(summary = "Get exercises by muscle group")
    public ResponseEntity<List<ExerciseDTO>> getExercisesByMuscleGroup(
            @PathVariable Exercise.MuscleGroup muscleGroup) {
        return ResponseEntity.ok(exerciseService.getExercisesByMuscleGroup(muscleGroup));
    }

    @PostMapping
    @Operation(summary = "Create a new exercise")
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return new ResponseEntity<>(exerciseService.createExercise(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update exercise")
    public ResponseEntity<ExerciseDTO> updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExerciseRequest request) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete exercise")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}