package edu.photo_school.course.domain;

import edu.photo_school.course.domain.enums.EnrollmentStatus;
import edu.photo_school.course.domain.events.EnrollmentCreatedEvent;
import edu.photo_school.course.domain.exception.InvalidEnrollmentStateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_enrollment_course_student",
                        columnNames = {"course_id", "student_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();

    /* =====================
       Factory method
       ===================== */
    public static Enrollment create(Long courseId, Long studentId) {
        if (courseId == null || studentId == null) {
            throw new IllegalArgumentException("CourseId and StudentId must not be null");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.courseId = courseId;
        enrollment.studentId = studentId;
        enrollment.status = EnrollmentStatus.PENDING;
        enrollment.createdAt = Instant.now();
        enrollment.updatedAt = enrollment.createdAt;


        // Публикуем событие
        enrollment.registerEvent(new EnrollmentCreatedEvent(
                enrollment.getId(),
                courseId,
                studentId
        ));

        return enrollment;
    }

    public void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }



    /* =====================
       Domain behavior
       ===================== */

    public void changeStatus(EnrollmentStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidEnrollmentStateException(this.status, newStatus);
        }

        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    /* =====================
       Convenience methods
       ===================== */

    public boolean isActive() {
        return this.status == EnrollmentStatus.CONFIRMED;
    }

    public boolean isCompleted() {
        return this.status == EnrollmentStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return this.status == EnrollmentStatus.CANCELLED;
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
