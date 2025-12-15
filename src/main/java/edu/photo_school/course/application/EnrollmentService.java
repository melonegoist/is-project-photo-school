package edu.photo_school.course.application;

import edu.photo_school.course.domain.Course;
import edu.photo_school.course.domain.Enrollment;
import edu.photo_school.course.domain.enums.EnrollmentStatus;
import edu.photo_school.course.infrastructure.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;

    // Явный конструктор для совместимости
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseService = courseService;
    }

    @Transactional
    public Enrollment enrollStudent(UUID courseId, UUID studentId) {
        Course course = courseService.getPublishedCourseById(courseId);

        if (!course.isAvailableForEnrollment()) {
            throw new IllegalStateException("Course is not available for enrollment");
        }

        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalStateException("Student already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.create(courseId, studentId);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment changeStatus(UUID enrollmentId, EnrollmentStatus newStatus) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        enrollment.changeStatus(newStatus);
        return enrollment;
    }

    @Transactional(readOnly = true)
    public Enrollment getEnrollment(UUID courseId, UUID studentId) {
        return enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }
}