package edu.photo_school.course.application;

import edu.photo_school.course.domain.Course;
import edu.photo_school.course.domain.CourseModule;
import edu.photo_school.course.infrastructure.repository.CourseModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CourseModuleService {

    private final CourseModuleRepository courseModuleRepository;
    private final CourseService courseService;

    // Явный конструктор для совместимости
    public CourseModuleService(CourseModuleRepository courseModuleRepository, CourseService courseService) {
        this.courseModuleRepository = courseModuleRepository;
        this.courseService = courseService;
    }

    @Transactional
    public CourseModule createModule(UUID courseId, String title, String description, Integer orderIndex) {
        Course course = courseService.getCourseById(courseId);

        CourseModule module = CourseModule.create(course, title, description, orderIndex);
        course.addModule(module);

        return courseModuleRepository.save(module);
    }

    @Transactional
    public CourseModule updateModule(
            UUID moduleId,
            String title,
            String description,
            Integer orderIndex
    ) {
        CourseModule module = getModuleById(moduleId);
        module.updateDetails(title, description, orderIndex);
        return module;
    }

    @Transactional(readOnly = true)
    public CourseModule getModuleById(UUID moduleId) {
        return courseModuleRepository.findById(moduleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course module not found")
                );
    }

    @Transactional(readOnly = true)
    public List<CourseModule> getModulesByCourseId(UUID courseId) {
        return courseModuleRepository.findAllByCourseIdOrderByOrderIndex(courseId);
    }

    @Transactional
    public void deleteModule(UUID moduleId) {
        CourseModule module = getModuleById(moduleId);
        courseModuleRepository.delete(module);
    }
}