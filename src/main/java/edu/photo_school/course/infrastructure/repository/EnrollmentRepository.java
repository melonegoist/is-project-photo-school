package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
}
