package edu.photo_school.course.application;

import edu.photo_school.course.domain.Course;
import edu.photo_school.course.domain.enums.CourseFormat;
import edu.photo_school.course.infrastructure.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    // Явный конструктор для совместимости
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Course createCourse(String title, String description, CourseFormat format, UUID teacherId) {
        Course course = Course.create(title, description, format, teacherId);
        return courseRepository.save(course);
    }

    @Transactional
    public Course publishCourse(UUID courseId) {
        Course course = getCourseById(courseId);
        course.publish();
        return course;
    }

    @Transactional
    public Course unpublishCourse(UUID courseId) {
        Course course = getCourseById(courseId);
        course.unpublish();
        return course;
    }

    @Transactional(readOnly = true)
    public List<Course> getPublishedCourses() {
        return courseRepository.findAllByPublishedTrue();
    }

    @Transactional(readOnly = true)
    public Course getPublishedCourseById(UUID courseId) {
        return courseRepository.findByIdAndPublishedTrue(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Published course not found"));
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesForTeacher(UUID teacherId) {
        return courseRepository.findAllByTeacherId(teacherId);
    }

    @Transactional(readOnly = true)
    public Course getCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }
}