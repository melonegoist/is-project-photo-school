package edu.photo_school.course.api;

import edu.photo_school.course.api.dto.CreateLessonRequest;
import edu.photo_school.course.api.dto.LessonResponse;
import edu.photo_school.course.application.LessonService;
import edu.photo_school.course.domain.Lesson;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    // Явный конструктор для совместимости
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse createLesson(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId,
            @RequestBody @Valid CreateLessonRequest request
    ) {
        Lesson lesson = lessonService.createLesson(
                moduleId,
                request.title(),
                request.description(),
                request.orderIndex(),
                request.durationMinutes(),
                request.videoUrl(),
                request.materialsUrl(),
                request.isPreview()
        );
        return LessonResponse.from(lesson);
    }

    @GetMapping
    public List<LessonResponse> getLessons(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId
    ) {
        return lessonService.getLessonsByModuleId(moduleId)
                .stream()
                .map(LessonResponse::from)
                .toList();
    }

    @GetMapping("/{lessonId}")
    public LessonResponse getLesson(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId,
            @PathVariable UUID lessonId
    ) {
        return LessonResponse.from(
                lessonService.getLessonById(lessonId)
        );
    }

    @GetMapping("/preview")
    public List<LessonResponse> getPreviewLessons(@PathVariable UUID courseId) {
        return lessonService.getPreviewLessonsByCourseId(courseId)
                .stream()
                .map(LessonResponse::from)
                .toList();
    }

    @PutMapping("/{lessonId}")
    public LessonResponse updateLesson(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId,
            @PathVariable UUID lessonId,
            @RequestBody @Valid CreateLessonRequest request
    ) {
        Lesson lesson = lessonService.updateLesson(
                lessonId,
                request.title(),
                request.description(),
                request.videoUrl(),
                request.materialsUrl(),
                request.durationMinutes(),
                request.orderIndex(),
                request.isPreview()
        );
        return LessonResponse.from(lesson);
    }

    @DeleteMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId,
            @PathVariable UUID lessonId
    ) {
        lessonService.deleteLesson(lessonId);
    }
}