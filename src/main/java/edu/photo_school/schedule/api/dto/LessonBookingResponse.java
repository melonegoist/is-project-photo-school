package edu.photo_school.schedule.api.dto;

import edu.photo_school.schedule.domain.LessonBooking;
import edu.photo_school.schedule.domain.enums.BookingStatus;

import java.time.Instant;
import java.util.UUID;

public record LessonBookingResponse(
        UUID id,
        UUID slotId,
        UUID studentId,
        BookingStatus status,
        Instant createdAt
) {
    public static LessonBookingResponse from(LessonBooking booking) {
        return new LessonBookingResponse(
                booking.getId(),
                booking.getSlotId(),
                booking.getStudentId(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}