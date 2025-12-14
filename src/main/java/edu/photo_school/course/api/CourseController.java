package edu.photo_school.course.api;

import edu.photo_school.course.api.dto.CourseResponse;
import edu.photo_school.course.api.dto.CreateCourseRequest;
import edu.photo_school.course.application.CourseService;
import edu.photo_school.course.application.EnrollmentService;
import edu.photo_school.course.domain.Course;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@RequestBody @Valid CreateCourseRequest request) {
        Course course = courseService.createCourse(
                request.title(),
                request.description(),
                request.format(),
                request.teacherId()
        );
        return CourseResponse.from(course);
    }

    @GetMapping("/published")
    public List<CourseResponse> getPublishedCourses() {
        return courseService.getPublishedCourses()
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    @GetMapping("/teacher/{teacherId}")
    public List<CourseResponse> getCoursesForTeacher(@PathVariable UUID teacherId) {
        return courseService.getCoursesForTeacher(teacherId)
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    @PatchMapping("/{courseId}/publish")
    public CourseResponse publishCourse(@PathVariable UUID courseId) {
        return CourseResponse.from(courseService.publishCourse(courseId));
    }

    @PatchMapping("/{courseId}/unpublish")
    public CourseResponse unpublishCourse(@PathVariable UUID courseId) {
        return CourseResponse.from(courseService.unpublishCourse(courseId));
    }

    @GetMapping("/{courseId}")
    public CourseResponse getCourse(@PathVariable UUID courseId) {
        return CourseResponse.from(courseService.getCourseById(courseId));
    }
}

