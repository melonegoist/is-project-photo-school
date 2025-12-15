package edu.photo_school.schedule.api.dto;

import jakarta.validation.constraints.NotNull;

public record BookSlotRequest(
        @NotNull
        Long studentId
) {
}