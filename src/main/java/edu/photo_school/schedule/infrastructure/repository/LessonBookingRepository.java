package edu.photo_school.schedule.infrastructure.repository;

import edu.photo_school.schedule.domain.LessonBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonBookingRepository extends JpaRepository<LessonBooking, UUID> {

    boolean existsBySlotIdAndStudentId(UUID slotId, UUID studentId);

    Optional<LessonBooking> findBySlotIdAndStudentId(UUID slotId, UUID studentId);

    List<LessonBooking> findByStudentId(UUID studentId);

    List<LessonBooking> findBySlotId(UUID slotId);

    long countBySlotId(UUID slotId);
}