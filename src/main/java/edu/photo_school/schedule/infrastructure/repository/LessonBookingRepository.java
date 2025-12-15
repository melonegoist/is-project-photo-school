package edu.photo_school.schedule.infrastructure.repository;

import edu.photo_school.schedule.domain.LessonBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonBookingRepository extends JpaRepository<LessonBooking, Long> {

    boolean existsBySlotIdAndStudentId(Long slotId, Long studentId);

    Optional<LessonBooking> findBySlotIdAndStudentId(Long slotId, Long studentId);

    List<LessonBooking> findByStudentId(Long studentId);

    List<LessonBooking> findBySlotId(Long slotId);

    long countBySlotId(Long slotId);

}
