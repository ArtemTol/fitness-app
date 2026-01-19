package com.fitness.gamification.controller;

import com.fitness.gamification.dto.CreateQuestRequest;
import com.fitness.gamification.dto.QuestDTO;
import com.fitness.gamification.dto.UpdateQuestRequest;
import com.fitness.gamification.service.QuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quests")
@RequiredArgsConstructor
@Tag(name = "Quests", description = "Quest management endpoints")
public class QuestController {

    private final QuestService questService;

    @GetMapping
    @Operation(summary = "Get all quests")
    public ResponseEntity<List<QuestDTO>> getAllQuests(
            @RequestParam(required = false) Boolean activeOnly) {
        return ResponseEntity.ok(questService.getAllQuests(activeOnly));
    }

    @GetMapping("/available")
    @Operation(summary = "Get currently available quests")
    public ResponseEntity<List<QuestDTO>> getAvailableQuests() {
        return ResponseEntity.ok(questService.getAvailableQuests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quest by ID")
    public ResponseEntity<QuestDTO> getQuestById(@PathVariable Long id) {
        return ResponseEntity.ok(questService.getQuestById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new quest")
    public ResponseEntity<QuestDTO> createQuest(@Valid @RequestBody CreateQuestRequest request) {
        return new ResponseEntity<>(questService.createQuest(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update quest")
    public ResponseEntity<QuestDTO> updateQuest(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuestRequest request) {
        return ResponseEntity.ok(questService.updateQuest(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quest")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        questService.deleteQuest(id);
        return ResponseEntity.noContent().build();
    }
}