package edu.photo_school.course.api;

import edu.photo_school.course.api.dto.EnrollRequest;
import edu.photo_school.course.api.dto.EnrollmentResponse;
import edu.photo_school.course.application.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll(
            @PathVariable Long courseId,
            @RequestBody @Valid EnrollRequest request
    ) {
        return EnrollmentResponse.from(
                enrollmentService.enrollStudent(courseId, request.studentId())
        );
    }

    @GetMapping("/students/{studentId}")
    public EnrollmentResponse getEnrollment(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ) {
        return EnrollmentResponse.from(
                enrollmentService.getEnrollment(courseId, studentId)
        );
    }
}
