package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.Enrollment;
import edu.photo_school.course.domain.enums.EnrollmentStatus;

import java.util.UUID;

public record EnrollmentResponse(
        UUID id,
        UUID courseId,
        UUID studentId,
        EnrollmentStatus status
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourseId(),
                enrollment.getStudentId(),
                enrollment.getStatus()
        );
    }
}
