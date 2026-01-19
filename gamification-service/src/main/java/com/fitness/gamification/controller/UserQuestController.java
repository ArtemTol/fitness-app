package com.fitness.gamification.controller;

import com.fitness.gamification.dto.UserQuestDTO;
import com.fitness.gamification.service.UserQuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/quests")
@RequiredArgsConstructor
@Tag(name = "User Quests", description = "User quest management endpoints")
public class UserQuestController {

    private final UserQuestService userQuestService;

    @GetMapping
    @Operation(summary = "Get user quests")
    public ResponseEntity<List<UserQuestDTO>> getUserQuests(@PathVariable Long userId) {
        return ResponseEntity.ok(userQuestService.getUserQuests(userId));
    }

    @GetMapping("/completed")
    @Operation(summary = "Get completed user quests")
    public ResponseEntity<List<UserQuestDTO>> getCompletedUserQuests(@PathVariable Long userId) {
        return ResponseEntity.ok(userQuestService.getCompletedUserQuests(userId));
    }

    @PostMapping("/{questId}/assign")
    @Operation(summary = "Assign quest to user")
    public ResponseEntity<UserQuestDTO> assignQuestToUser(
            @PathVariable Long userId,
            @PathVariable Long questId) {
        return new ResponseEntity<>(
                userQuestService.assignQuestToUser(userId, questId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{questId}/progress")
    @Operation(summary = "Update quest progress")
    public ResponseEntity<UserQuestDTO> updateQuestProgress(
            @PathVariable Long userId,
            @PathVariable Long questId,
            @RequestParam Integer progress) {
        return ResponseEntity.ok(
                userQuestService.updateQuestProgress(userId, questId, progress)
        );
    }

    @PostMapping("/{questId}/claim")
    @Operation(summary = "Claim quest reward")
    public ResponseEntity<UserQuestDTO> claimQuestReward(
            @PathVariable Long userId,
            @PathVariable Long questId) {
        return ResponseEntity.ok(
                userQuestService.claimQuestReward(userId, questId)
        );
    }
}