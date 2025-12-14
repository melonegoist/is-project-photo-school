package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findAllByPublishedTrue();

    List<Course> findAllByTeacherId(UUID teacherId);

    Optional<Course> findByIdAndPublishedTrue(UUID id);
}
