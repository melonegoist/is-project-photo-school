package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.Lesson;

import java.util.UUID;

public record LessonResponse(
        UUID id,
        String title,
        String description,
        String videoUrl,
        String materialsUrl,
        Integer durationMinutes,
        Integer orderIndex,
        boolean isPreview,
        UUID moduleId
) {
    public static LessonResponse from(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getVideoUrl(),
                lesson.getMaterialsUrl(),
                lesson.getDurationMinutes(),
                lesson.getOrderIndex(),
                lesson.isPreview(),
                lesson.getModule().getId()
        );
    }
}