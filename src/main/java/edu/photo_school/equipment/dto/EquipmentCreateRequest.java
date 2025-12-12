package edu.photo_school.equipment.dto;

import edu.photo_school.equipment.domain.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EquipmentCreateRequest(
        @NotBlank String name,
        String description,
        EquipmentStatus status,
        @NotNull @Positive BigDecimal hourlyRate
) {
}
