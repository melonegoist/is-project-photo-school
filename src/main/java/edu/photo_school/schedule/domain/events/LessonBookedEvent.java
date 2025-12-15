package edu.photo_school.schedule.domain.events;

import edu.photo_school.course.domain.DomainEvent;

public class LessonBookedEvent extends DomainEvent {
    private final Long bookingId;
    private final Long slotId;
    private final Long studentId;

    public LessonBookedEvent(Long bookingId, Long slotId, Long studentId) {
        this.bookingId = bookingId;
        this.slotId = slotId;
        this.studentId = studentId;
    }

    @Override
    public String getEventType() {
        return "schedule.lesson.booked";
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public Long getStudentId() {
        return studentId;
    }
}