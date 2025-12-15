package edu.photo_school.course.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateModuleRequest(
        @NotBlank
        String title,

        String description,

        @NotNull
        Integer orderIndex
) {
}

