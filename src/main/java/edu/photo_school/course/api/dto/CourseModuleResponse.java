package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.CourseModule;

import java.util.List;

public record CourseModuleResponse(
        Long id,
        String title,
        String description,
        Integer orderIndex,
        Long courseId,
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
