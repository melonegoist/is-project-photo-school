package edu.photo_school.course.api;

import edu.photo_school.course.api.dto.CreateLessonRequest;
import edu.photo_school.course.api.dto.LessonResponse;
import edu.photo_school.course.application.LessonService;
import edu.photo_school.course.domain.Lesson;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse createLesson(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
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
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        return lessonService.getLessonsByModuleId(moduleId)
                .stream()
                .map(LessonResponse::from)
                .toList();
    }

    @GetMapping("/{lessonId}")
    public LessonResponse getLesson(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @PathVariable Long lessonId
    ) {
        return LessonResponse.from(
                lessonService.getLessonById(lessonId)
        );
    }

    @GetMapping("/preview")
    public List<LessonResponse> getPreviewLessons(@PathVariable Long courseId) {
        return lessonService.getPreviewLessonsByCourseId(courseId)
                .stream()
                .map(LessonResponse::from)
                .toList();
    }

    @PutMapping("/{lessonId}")
    public LessonResponse updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @PathVariable Long lessonId,
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
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @PathVariable Long lessonId
    ) {
        lessonService.deleteLesson(lessonId);
    }

}
