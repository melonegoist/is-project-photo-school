package edu.photo_school.course.api.dto;

import edu.photo_school.course.domain.Course;
import edu.photo_school.course.domain.enums.CourseFormat;

import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String description,
        CourseFormat format,
        boolean published,
        Long teacherId,
        List<CourseModuleResponse> modules
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getFormat(),
                course.isPublished(),
                course.getTeacherId(),
                course.getModules().stream().map(CourseModuleResponse::from).toList()
        );
    }
}