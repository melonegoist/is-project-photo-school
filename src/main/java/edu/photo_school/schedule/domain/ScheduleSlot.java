package edu.photo_school.schedule.domain;

import edu.photo_school.schedule.domain.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_slots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleSlot {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "max_students")
    private Integer maxStudents = 1;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "price_amount")
    private Double priceAmount;

    @Column(name = "price_currency")
    private String priceCurrency;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static ScheduleSlot create(
            Long teacherId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Long lessonId,
            Integer maxStudents,
            Double priceAmount,
            String priceCurrency,
            String description
    ) {
        validateSlotTimes(startTime, endTime);

        ScheduleSlot slot = new ScheduleSlot();
        slot.teacherId = teacherId;
        slot.startTime = startTime;
        slot.endTime = endTime;
        slot.lessonId = lessonId;
        slot.maxStudents = maxStudents != null ? maxStudents : 1;
        slot.priceAmount = priceAmount;
        slot.priceCurrency = priceCurrency;
        slot.description = description;
        slot.createdAt = Instant.now();
        slot.updatedAt = slot.createdAt;

        return slot;
    }

    private static void validateSlotTimes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end time must not be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create slot in the past");
        }
    }

    public boolean isAvailable() {
        return status == SlotStatus.AVAILABLE ||
                (status == SlotStatus.PARTIALLY_BOOKED && currentStudents < maxStudents);
    }

    public void bookSlot() {
        if (!isAvailable()) {
            throw new IllegalStateException("Slot is not available for booking");
        }

        currentStudents++;

        if (currentStudents.equals(maxStudents)) {
            status = SlotStatus.BOOKED;
        } else {
            status = SlotStatus.PARTIALLY_BOOKED;
        }

        updatedAt = Instant.now();
    }

    public void cancelBooking() {
        if (currentStudents <= 0) {
            throw new IllegalStateException("No bookings to cancel");
        }

        currentStudents--;

        if (currentStudents == 0) {
            status = SlotStatus.AVAILABLE;
        } else if (currentStudents < maxStudents) {
            status = SlotStatus.PARTIALLY_BOOKED;
        }

        updatedAt = Instant.now();
    }

    public void markAsCompleted() {
        if (startTime.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot complete future slot");
        }
        status = SlotStatus.COMPLETED;
        updatedAt = Instant.now();
    }

    public void markAsCancelled() {
        status = SlotStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    // Явные геттеры для полей, которые Lombok может не обработать
    public Integer getCurrentStudents() {
        return currentStudents;
    }

    public Double getPriceAmount() {
        return priceAmount;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public Long getId() {
        return id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public Long getLessonId() {
        return lessonId;
    }
}
