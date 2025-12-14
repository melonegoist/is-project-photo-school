package edu.photo_school.schedule.domain;

import edu.photo_school.course.domain.DomainEvent;
import edu.photo_school.schedule.domain.enums.BookingStatus;
import edu.photo_school.schedule.domain.events.LessonBookedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lesson_bookings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"slot_id", "student_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LessonBooking {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "slot_id", nullable = false)
    private UUID slotId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public static LessonBooking create(UUID slotId, UUID studentId) {
        LessonBooking booking = new LessonBooking();
        booking.slotId = slotId;
        booking.studentId = studentId;
        booking.status = BookingStatus.CONFIRMED;
        booking.createdAt = Instant.now();
        booking.updatedAt = booking.createdAt;

        // Публикуем событие
        booking.registerEvent(new LessonBookedEvent(
                booking.id,
                slotId,
                studentId
        ));

        return booking;
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

    public void cancel() {
        if (status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        if (status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed booking");
        }

        status = BookingStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be completed");
        }

        status = BookingStatus.COMPLETED;
        updatedAt = Instant.now();
    }

    public void markAsNoShow() {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be marked as no-show");
        }

        status = BookingStatus.NO_SHOW;
        updatedAt = Instant.now();
    }

    // Явные геттеры для полей
    public UUID getId() {
        return id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getSlotId() {
        return slotId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}