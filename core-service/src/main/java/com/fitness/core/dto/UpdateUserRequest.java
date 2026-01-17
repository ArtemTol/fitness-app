package com.fitness.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Double weightKg;
    private Integer heightCm;

    private String avatarUrl;
}
