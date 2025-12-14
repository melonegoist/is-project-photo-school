package edu.photo_school.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course_modules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseModule {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Lesson> lessons = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /* =====================
       Factory method
       ===================== */

    public static CourseModule create(Course course, String title, String description, Integer orderIndex) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Module title must not be empty");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course must not be null");
        }
        if (orderIndex == null || orderIndex < 0) {
            throw new IllegalArgumentException("Order index must be non-negative");
        }

        CourseModule module = new CourseModule();
        module.title = title;
        module.description = description;
        module.orderIndex = orderIndex;
        module.course = course;
        module.createdAt = Instant.now();
        module.updatedAt = module.createdAt;

        return module;
    }

    /* =====================
       Domain behavior
       ===================== */

    public void updateDetails(String title, String description, Integer orderIndex) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (orderIndex != null && orderIndex >= 0) {
            this.orderIndex = orderIndex;
        }
        this.updatedAt = Instant.now();
    }

    public void addLesson(Lesson lesson) {
        if (lesson != null && !lessons.contains(lesson)) {
            lessons.add(lesson);
            this.updatedAt = Instant.now();
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getOrderIndex() { return orderIndex; }
    public Course getCourse() { return course; }
    public List<Lesson> getLessons() { return lessons; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}