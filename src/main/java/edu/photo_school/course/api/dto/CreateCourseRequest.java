package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.enums.CourseFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(

        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        CourseFormat format,

        @NotNull
        Long teacherId
) {
}
