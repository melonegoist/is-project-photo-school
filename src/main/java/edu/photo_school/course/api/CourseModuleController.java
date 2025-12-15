package edu.photo_school.course.api;

import edu.photo_school.course.api.dto.CourseModuleResponse;
import edu.photo_school.course.api.dto.CreateModuleRequest;
import edu.photo_school.course.application.CourseModuleService;
import edu.photo_school.course.domain.CourseModule;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules")
public class CourseModuleController {

    private final CourseModuleService courseModuleService;

    // Явный конструктор для совместимости
    public CourseModuleController(CourseModuleService courseModuleService) {
        this.courseModuleService = courseModuleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseModuleResponse createModule(
            @PathVariable Long courseId,
            @RequestBody @Valid CreateModuleRequest request
    ) {
        CourseModule module = courseModuleService.createModule(
                courseId,
                request.title(),
                request.description(),
                request.orderIndex()
        );
        return CourseModuleResponse.from(module);
    }

    @GetMapping
    public List<CourseModuleResponse> getModules(@PathVariable Long courseId) {
        return courseModuleService.getModulesByCourseId(courseId)
                .stream()
                .map(CourseModuleResponse::from)
                .toList();
    }

    @GetMapping("/{moduleId}")
    public CourseModuleResponse getModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        return CourseModuleResponse.from(
                courseModuleService.getModuleById(moduleId)
        );
    }

    @PutMapping("/{moduleId}")
    public CourseModuleResponse updateModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @RequestBody @Valid CreateModuleRequest request
    ) {
        CourseModule module = courseModuleService.updateModule(
                moduleId,
                request.title(),
                request.description(),
                request.orderIndex()
        );
        return CourseModuleResponse.from(module);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        courseModuleService.deleteModule(moduleId);
    }
}