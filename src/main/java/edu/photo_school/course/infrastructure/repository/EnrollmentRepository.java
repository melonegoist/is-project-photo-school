package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    boolean existsByCourseIdAndStudentId(UUID courseId, UUID studentId);

    Optional<Enrollment> findByCourseIdAndStudentId(UUID courseId, UUID studentId);
}
