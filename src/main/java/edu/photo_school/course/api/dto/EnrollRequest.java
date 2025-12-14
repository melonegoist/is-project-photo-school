package edu.photo_school.course.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnrollRequest(
        @NotNull
        UUID studentId
) {
}
