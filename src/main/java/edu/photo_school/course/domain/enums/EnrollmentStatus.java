package edu.photo_school.course.domain.enums;

public enum EnrollmentStatus {

    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(EnrollmentStatus target) {
        return switch (this) {
            case PENDING -> target == CONFIRMED || target == CANCELLED;

            case CONFIRMED -> target == COMPLETED || target == CANCELLED;

            case COMPLETED, CANCELLED -> false;
        };
    }
}
