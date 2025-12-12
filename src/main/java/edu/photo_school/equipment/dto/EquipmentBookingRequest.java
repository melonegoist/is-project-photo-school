package edu.photo_school.equipment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record EquipmentBookingRequest(
        @NotNull UUID userId,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
}
