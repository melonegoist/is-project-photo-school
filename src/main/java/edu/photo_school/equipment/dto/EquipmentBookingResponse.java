package edu.photo_school.equipment.dto;

import edu.photo_school.equipment.domain.EquipmentBookingStatus;

import java.time.LocalDateTime;

public record EquipmentBookingResponse(
        Long id,
        Long equipmentId,
        Long userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        EquipmentBookingStatus status
) {
}
