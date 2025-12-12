package edu.photo_school.equipment.dto;

import edu.photo_school.equipment.domain.EquipmentStatus;

import java.math.BigDecimal;

public record EquipmentResponse(
        Long id,
        String name,
        String description,
        EquipmentStatus status,
        BigDecimal hourlyRate
) {
}
