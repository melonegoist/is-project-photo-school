package edu.photo_school.equipment.dto;

import edu.photo_school.equipment.domain.EquipmentBookingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EquipmentBookingResponse(
        Long id,
        Long equipmentId,
        UUID userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        EquipmentBookingStatus status
) {
}
