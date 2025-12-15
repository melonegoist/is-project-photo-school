package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.CourseModule;
import edu.photo_school.course.domain.Lesson;

import java.util.List;
import java.util.UUID;

public record CourseModuleResponse(
        UUID id,
        String title,
        String description,
        Integer orderIndex,
        UUID courseId,
        List<LessonResponse> lessons
) {
    public static CourseModuleResponse from(CourseModule module) {
        return new CourseModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex(),
                module.getCourse().getId(),
                module.getLessons().stream()
                        .map(LessonResponse::from)
                        .toList()
        );
    }
}