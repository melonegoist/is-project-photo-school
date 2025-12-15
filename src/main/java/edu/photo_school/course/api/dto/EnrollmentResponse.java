package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.Enrollment;
import edu.photo_school.course.domain.enums.EnrollmentStatus;

public record EnrollmentResponse(
        Long id,
        Long courseId,
        Long studentId,
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
