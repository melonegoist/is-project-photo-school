package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {
    List<CourseModule> findAllByCourseIdOrderByOrderIndex(Long courseId);
}
