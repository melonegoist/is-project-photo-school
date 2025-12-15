package edu.photo_school.course.application;

import edu.photo_school.course.domain.Course;
import edu.photo_school.course.domain.enums.CourseFormat;
import edu.photo_school.course.infrastructure.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    // Явный конструктор для совместимости
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Course createCourse(String title, String description, CourseFormat format, Long teacherId) {
        Course course = Course.create(title, description, format, teacherId);
        return courseRepository.save(course);
    }

    @Transactional
    public Course publishCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.publish();
        return course;
    }

    @Transactional
    public Course unpublishCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.unpublish();
        return course;
    }

    @Transactional(readOnly = true)
    public List<Course> getPublishedCourses() {
        return courseRepository.findAllByPublishedTrue();
    }

    @Transactional(readOnly = true)
    public Course getPublishedCourseById(Long courseId) {
        return courseRepository.findByIdAndPublishedTrue(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Published course not found"));
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesForTeacher(Long teacherId) {
        return courseRepository.findAllByTeacherId(teacherId);
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

}
