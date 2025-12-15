package edu.photo_school.course.domain.exception;

import edu.photo_school.course.domain.enums.EnrollmentStatus;

public class InvalidEnrollmentStateException extends RuntimeException {

    private final EnrollmentStatus from;
    private final EnrollmentStatus to;

    public InvalidEnrollmentStateException(
            EnrollmentStatus from,
            EnrollmentStatus to
    ) {
        super(
                String.format(
                        "Invalid enrollment status transition: %s -> %s",
                        from,
                        to
                )
        );
        this.from = from;
        this.to = to;
    }

    public EnrollmentStatus getFrom() {
        return from;
    }

    public EnrollmentStatus getTo() {
        return to;
    }
}
