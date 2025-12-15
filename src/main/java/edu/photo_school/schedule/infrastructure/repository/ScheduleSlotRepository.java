package edu.photo_school.schedule.infrastructure.repository;

import edu.photo_school.schedule.domain.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {

    List<ScheduleSlot> findByTeacherIdAndStartTimeBetween(
            Long teacherId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ScheduleSlot> findByTeacherIdAndStartTimeAfter(
            Long teacherId,
            LocalDateTime start
    );

    @Query("SELECT s FROM ScheduleSlot s WHERE " +
            "s.teacherId = :teacherId AND " +
            "s.startTime >= :start AND " +
            "s.endTime <= :end AND " +
            "s.status IN (edu.photo_school.schedule.domain.enums.SlotStatus.AVAILABLE, " +
            "edu.photo_school.schedule.domain.enums.SlotStatus.PARTIALLY_BOOKED)")
    List<ScheduleSlot> findAvailableSlotsByTeacherIdAndTimeRange(
            @Param("teacherId") Long teacherId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    Optional<ScheduleSlot> findByIdAndTeacherId(Long id, Long teacherId);
}