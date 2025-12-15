package edu.photo_school.schedule.api.dto;

import edu.photo_school.schedule.domain.LessonBooking;
import edu.photo_school.schedule.domain.enums.BookingStatus;

import java.time.Instant;

public record LessonBookingResponse(
        Long id,
        Long slotId,
        Long studentId,
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