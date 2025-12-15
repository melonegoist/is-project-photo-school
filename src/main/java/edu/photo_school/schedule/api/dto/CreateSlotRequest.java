package edu.photo_school.schedule.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateSlotRequest(
        @NotNull
        Long teacherId,

        @NotNull
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @NotNull
        @Future(message = "End time must be in the future")
        LocalDateTime endTime,

        Long lessonId,

        @Positive
        Integer maxStudents,

        Double priceAmount,

        String priceCurrency,

        String description
) {}