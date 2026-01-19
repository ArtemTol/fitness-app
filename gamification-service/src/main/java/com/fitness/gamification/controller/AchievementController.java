package com.fitness.gamification.controller;

import com.fitness.gamification.dto.AchievementDTO;
import com.fitness.gamification.dto.CreateAchievementRequest;
import com.fitness.gamification.dto.UpdateAchievementRequest;
import com.fitness.gamification.service.AchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
@Tag(name = "Achievements", description = "Achievement management endpoints")
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    @Operation(summary = "Get all achievements")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements(
            @RequestParam(required = false) Boolean activeOnly) {
        return ResponseEntity.ok(achievementService.getAllAchievements(activeOnly));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get achievement by ID")
    public ResponseEntity<AchievementDTO> getAchievementById(@PathVariable Long id) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new achievement")
    public ResponseEntity<AchievementDTO> createAchievement(
            @Valid @RequestBody CreateAchievementRequest request) {
        return new ResponseEntity<>(
                achievementService.createAchievement(request),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update achievement")
    public ResponseEntity<AchievementDTO> updateAchievement(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAchievementRequest request) {
        return ResponseEntity.ok(achievementService.updateAchievement(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete achievement")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}