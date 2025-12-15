package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByPublishedTrue();

    List<Course> findAllByTeacherId(Long teacherId);

    Optional<Course> findByIdAndPublishedTrue(Long id);
}
