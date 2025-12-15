package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByModuleIdOrderByOrderIndex(Long moduleId);

    List<Lesson> findByModuleCourseIdAndIsPreviewTrue(Long courseId);
}
