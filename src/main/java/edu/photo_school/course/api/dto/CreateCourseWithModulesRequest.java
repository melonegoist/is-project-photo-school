package edu.photo_school.course.api.dto;
import edu.photo_school.course.domain.enums.CourseFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateCourseWithModulesRequest(
        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        CourseFormat format,

        @NotNull
        UUID teacherId,

        @Valid
        @Size(min = 1, message = "Course must have at least one module")
        List<ModuleWithLessonsRequest> modules
) {
    record ModuleWithLessonsRequest(
            @NotBlank
            String title,

            String description,

            @NotNull
            Integer orderIndex,

            @Valid
            List<LessonRequest> lessons
    ) {}

    record LessonRequest(
            @NotBlank
            String title,

            String description,

            @NotNull
            Integer orderIndex,

            Integer durationMinutes,

            String videoUrl,

            String materialsUrl,

            Boolean isPreview
    ) {}
}