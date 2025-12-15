package edu.photo_school.course.api.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollRequest(
        @NotNull
        Long studentId
) {
}
