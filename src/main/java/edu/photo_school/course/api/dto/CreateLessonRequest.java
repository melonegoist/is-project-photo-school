package edu.photo_school.course.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLessonRequest(
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

