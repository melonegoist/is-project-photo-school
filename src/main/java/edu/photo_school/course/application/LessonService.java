package edu.photo_school.course.application;

import edu.photo_school.course.domain.CourseModule;
import edu.photo_school.course.domain.Lesson;
import edu.photo_school.course.infrastructure.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseModuleService courseModuleService;

    public LessonService(CourseModuleService courseModuleService, LessonRepository lessonRepository) {
        this.courseModuleService = courseModuleService;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    public Lesson createLesson(
            Long moduleId,
            String title,
            String description,
            Integer orderIndex,
            Integer durationMinutes,
            String videoUrl,
            String materialsUrl,
            Boolean isPreview
    ) {
        CourseModule module = courseModuleService.getModuleById(moduleId);

        Lesson lesson = Lesson.create(module, title, description, orderIndex, durationMinutes);

        if (videoUrl != null) lesson.updateDetails(null, null, videoUrl, null, null, null, null);
        if (materialsUrl != null) lesson.updateDetails(null, null, null, materialsUrl, null, null, null);
        if (isPreview != null && isPreview) lesson.markAsPreview();

        module.addLesson(lesson);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson updateLesson(
            Long lessonId,
            String title,
            String description,
            String videoUrl,
            String materialsUrl,
            Integer durationMinutes,
            Integer orderIndex,
            Boolean isPreview
    ) {
        Lesson lesson = getLessonById(lessonId);
        lesson.updateDetails(title, description, videoUrl, materialsUrl, durationMinutes, orderIndex, isPreview);
        return lesson;
    }

    @Transactional(readOnly = true)
    public Lesson getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson not found")
                );
    }

    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByModuleId(Long moduleId) {
        return lessonRepository.findAllByModuleIdOrderByOrderIndex(moduleId);
    }

    @Transactional(readOnly = true)
    public List<Lesson> getPreviewLessonsByCourseId(Long courseId) {
        return lessonRepository.findByModuleCourseIdAndIsPreviewTrue(courseId);
    }

    @Transactional
    public void deleteLesson(Long lessonId) {
        Lesson lesson = getLessonById(lessonId);
        lessonRepository.delete(lesson);
    }

}
