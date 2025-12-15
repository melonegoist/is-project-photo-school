package edu.photo_school.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "lessons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "materials_url")
    private String materialsUrl;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "is_preview", nullable = false)
    private boolean isPreview = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Lesson create(
            CourseModule module,
            String title,
            String description,
            Integer orderIndex,
            Integer durationMinutes
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Lesson title must not be empty");
        }
        if (module == null) {
            throw new IllegalArgumentException("Module must not be null");
        }
        if (orderIndex == null || orderIndex < 0) {
            throw new IllegalArgumentException("Order index must be non-negative");
        }

        Lesson lesson = new Lesson();
        lesson.title = title;
        lesson.description = description;
        lesson.orderIndex = orderIndex;
        lesson.durationMinutes = durationMinutes;
        lesson.module = module;
        lesson.createdAt = Instant.now();
        lesson.updatedAt = lesson.createdAt;

        return lesson;
    }

    public void updateDetails(
            String title,
            String description,
            String videoUrl,
            String materialsUrl,
            Integer durationMinutes,
            Integer orderIndex,
            Boolean isPreview
    ) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (videoUrl != null) {
            this.videoUrl = videoUrl;
        }
        if (materialsUrl != null) {
            this.materialsUrl = materialsUrl;
        }
        if (durationMinutes != null && durationMinutes > 0) {
            this.durationMinutes = durationMinutes;
        }
        if (orderIndex != null && orderIndex >= 0) {
            this.orderIndex = orderIndex;
        }
        if (isPreview != null) {
            this.isPreview = isPreview;
        }
        this.updatedAt = Instant.now();
    }

    public void markAsPreview() {
        this.isPreview = true;
        this.updatedAt = Instant.now();
    }

    public void unmarkAsPreview() {
        this.isPreview = false;
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getMaterialsUrl() {
        return materialsUrl;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public CourseModule getModule() {
        return module;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
