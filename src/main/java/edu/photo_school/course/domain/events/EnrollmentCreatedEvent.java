package edu.photo_school.course.domain.events;


import edu.photo_school.course.domain.DomainEvent;

import java.util.UUID;

public class EnrollmentCreatedEvent extends DomainEvent {
    private final UUID enrollmentId;
    private final UUID courseId;
    private final UUID studentId;

    public EnrollmentCreatedEvent(UUID enrollmentId, UUID courseId, UUID studentId) {
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.studentId = studentId;
    }

    @Override
    public String getEventType() {
        return "course.enrollment.created";
    }

    public UUID getEnrollmentId() { return enrollmentId; }
    public UUID getCourseId() { return courseId; }
    public UUID getStudentId() { return studentId; }
}