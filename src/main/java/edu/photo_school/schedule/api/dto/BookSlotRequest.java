package edu.photo_school.schedule.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookSlotRequest(
        @NotNull
        UUID studentId
) {}