package edu.photo_school.course.infrastructure.repository;

import edu.photo_school.course.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findAllByModuleId(UUID moduleId);
    List<Lesson> findAllByModuleIdOrderByOrderIndex(UUID moduleId);
    List<Lesson> findByModuleCourseIdAndIsPreviewTrue(UUID courseId);
}