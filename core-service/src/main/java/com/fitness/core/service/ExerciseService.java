package com.fitness.core.service;

import com.fitness.core.dto.CreateExerciseRequest;
import com.fitness.core.dto.ExerciseDTO;
import com.fitness.core.dto.UpdateExerciseRequest;
import com.fitness.core.exception.ResourceNotFoundException;
import com.fitness.core.model.Exercise;
import com.fitness.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final KafkaService kafkaService;

    public List<ExerciseDTO> getAllExercises() {
        log.info("Получение всех упражнений");
        ExerciseDTO body = new ExerciseDTO();
        body.setId(new Random().nextLong());
        body.setCreatedAt(LocalDateTime.now());
        kafkaService.sendKafkaMessage(body);
        return exerciseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExerciseDTO getExerciseById(Long id) {
        log.info("Получение упражнения по id: {}", id);
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Упражнение не найдено с id: " + id));
        return convertToDTO(exercise);
    }

    public List<ExerciseDTO> getExercisesByMuscleGroup(Exercise.MuscleGroup muscleGroup) {
        log.info("Получение упражнений по группе мышц: {}", muscleGroup);
        return exerciseRepository.findByMuscleGroup(muscleGroup).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExerciseDTO createExercise(CreateExerciseRequest request) {
        log.info("Создание нового упражнения: {}", request.getName());

        Exercise exercise = new Exercise();
        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setType(request.getType());
        exercise.setImageUrl(request.getImageUrl());
        exercise.setMuscleGroup(request.getMuscleGroup());
        exercise.setRequiresEquipment(request.getRequiresEquipment());

        Exercise savedExercise = exerciseRepository.save(exercise);
        log.info("Упражнение успешно создано с id: {}", savedExercise.getId());
        return convertToDTO(savedExercise);
    }

    @Transactional
    public ExerciseDTO updateExercise(Long id, UpdateExerciseRequest request) {
        log.info("Обновление упражнения с id: {}", id);

        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Упражнение не найдено с id: " + id));

        if (request.getName() != null) {
            exercise.setName(request.getName());
        }
        if (request.getDescription() != null) {
            exercise.setDescription(request.getDescription());
        }
        if (request.getType() != null) {
            exercise.setType(request.getType());
        }
        if (request.getImageUrl() != null) {
            exercise.setImageUrl(request.getImageUrl());
        }
        if (request.getMuscleGroup() != null) {
            exercise.setMuscleGroup(request.getMuscleGroup());
        }
        if (request.getRequiresEquipment() != null) {
            exercise.setRequiresEquipment(request.getRequiresEquipment());
        }

        Exercise updatedExercise = exerciseRepository.save(exercise);
        log.info("Упражнение успешно обновлено с id: {}", id);

        return convertToDTO(updatedExercise);
    }

    @Transactional
    public void deleteExercise(Long id) {
        log.info("Удаление упражнения с id: {}", id);

        if (!exerciseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Упражнение не найдено с id: " + id);
        }

        exerciseRepository.deleteById(id);
        log.info("Упражнение успешно удалено с id: {}", id);
    }

    private ExerciseDTO convertToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setType(exercise.getType());
        dto.setImageUrl(exercise.getImageUrl());
        dto.setMuscleGroup(exercise.getMuscleGroup());
        dto.setRequiresEquipment(exercise.getRequiresEquipment());
        return dto;
    }
}