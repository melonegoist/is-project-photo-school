package edu.photo_school.course.domain.events;

import edu.photo_school.course.domain.DomainEvent;

public class EnrollmentCreatedEvent extends DomainEvent {
    private final Long enrollmentId;
    private final Long courseId;
    private final Long studentId;

    public EnrollmentCreatedEvent(Long enrollmentId, Long courseId, Long studentId) {
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.studentId = studentId;
    }

    @Override
    public String getEventType() {
        return "course.enrollment.created";
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

}
