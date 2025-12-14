package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseModuleRepository extends JpaRepository<CourseModule, UUID> {
    List<CourseModule> findAllByCourseId(UUID courseId);
    List<CourseModule> findAllByCourseIdOrderByOrderIndex(UUID courseId);
}