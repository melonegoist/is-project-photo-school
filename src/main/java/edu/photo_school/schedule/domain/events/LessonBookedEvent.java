package edu.photo_school.schedule.domain.events;

import edu.photo_school.course.domain.DomainEvent;

import java.util.UUID;

public class LessonBookedEvent extends DomainEvent {
    private final UUID bookingId;
    private final UUID slotId;
    private final UUID studentId;

    public LessonBookedEvent(UUID bookingId, UUID slotId, UUID studentId) {
        this.bookingId = bookingId;
        this.slotId = slotId;
        this.studentId = studentId;
    }

    @Override
    public String getEventType() {
        return "schedule.lesson.booked";
    }

    public UUID getBookingId() { return bookingId; }
    public UUID getSlotId() { return slotId; }
    public UUID getStudentId() { return studentId; }
}