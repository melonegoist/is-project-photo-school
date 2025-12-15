package edu.photo_school.schedule.api.dto;

import edu.photo_school.schedule.domain.ScheduleSlot;
import edu.photo_school.schedule.domain.enums.SlotStatus;

import java.time.LocalDateTime;

public record ScheduleSlotResponse(
        Long id,
        Long teacherId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        SlotStatus status,
        Long lessonId,
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