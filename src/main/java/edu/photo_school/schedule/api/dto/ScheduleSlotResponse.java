package edu.photo_school.schedule.api.dto;

import edu.photo_school.schedule.domain.ScheduleSlot;
import edu.photo_school.schedule.domain.enums.SlotStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleSlotResponse(
        UUID id,
        UUID teacherId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        SlotStatus status,
        UUID lessonId,
        Integer maxStudents,
        Integer currentStudents,
        Double priceAmount,
        String priceCurrency,
        String description
) {
    public static ScheduleSlotResponse from(ScheduleSlot slot) {
        return new ScheduleSlotResponse(
                slot.getId(),
                slot.getTeacherId(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getStatus(),
                slot.getLessonId(),
                slot.getMaxStudents(),
                slot.getCurrentStudents(),
                slot.getPriceAmount(),
                slot.getPriceCurrency(),
                slot.getDescription()
        );
    }
}