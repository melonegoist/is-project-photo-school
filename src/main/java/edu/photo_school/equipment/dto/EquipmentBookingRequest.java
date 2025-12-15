package edu.photo_school.equipment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EquipmentBookingRequest(
        @NotNull Long userId,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
}
