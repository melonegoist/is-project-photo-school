package edu.photo_school.course.domain;

import edu.photo_school.course.domain.enums.CourseFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseFormat format;

    @Column(name = "teacher_id", nullable = false)
    private UUID teacherId;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<CourseModule> modules = new ArrayList<>();

    @Column(nullable = false)
    private boolean published;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /* =====================
       Factory
       ===================== */

    public static Course create(
            String title,
            String description,
            CourseFormat format,
            UUID teacherId
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Course title must not be empty");
        }
        if (format == null) {
            throw new IllegalArgumentException("Course format must not be null");
        }
        if (teacherId == null) {
            throw new IllegalArgumentException("TeacherId must not be null");
        }

        Course course = new Course();
        course.title = title;
        course.description = description;
        course.format = format;
        course.teacherId = teacherId;
        course.published = false;
        course.createdAt = Instant.now();
        course.updatedAt = course.createdAt;

        return course;
    }

    /* =====================
       Domain behavior
       ===================== */

    public void addModule(CourseModule module) {
        if (module != null && !modules.contains(module)) {
            modules.add(module);
            this.updatedAt = Instant.now();
        }
    }

    public CourseModule getModuleById(UUID moduleId) {
        return modules.stream()
                .filter(module -> module.getId().equals(moduleId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Module not found in this course")
                );
    }

    public void publish() {
        if (this.title == null || this.title.isBlank()) {
            throw new IllegalStateException("Cannot publish course without title");
        }
        this.published = true;
        this.updatedAt = Instant.now();
    }

    public void unpublish() {
        this.published = false;
        this.updatedAt = Instant.now();
    }

    public boolean isAvailableForEnrollment() {
        return this.published;
    }

    public void updateDetails(String title, String description) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public CourseFormat getFormat() { return format; }
    public UUID getTeacherId() { return teacherId; }
    public boolean isPublished() { return published; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<CourseModule> getModules() { return new ArrayList<>(modules); }
}
