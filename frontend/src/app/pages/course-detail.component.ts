import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {Course, CourseModule, Lesson} from '../models/models';
import {CourseService, CreateLessonPayload, CreateModulePayload} from '../services/course.service';

@Component({
  standalone: true,
  selector: 'app-course-detail',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './course-detail.component.html',
  styleUrl: './course-detail.component.scss'
})
export class CourseDetailComponent implements OnInit {
  course?: Course;
  courseId!: string;
  moduleForm: CreateModulePayload = {title: '', description: '', orderIndex: 1};
  lessonForm: CreateLessonPayload & { moduleId?: string } = {
    title: '',
    description: '',
    orderIndex: 1,
    durationMinutes: 90,
    isPreview: false
  };
  enrollStudentId = '';
  message?: string;
  error?: string;

  constructor(private readonly route: ActivatedRoute, private readonly courseService: CourseService) {
  }

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('id') as string;
    this.loadCourse();
  }

  loadCourse(): void {
    this.courseService.getCourse(this.courseId).subscribe({
      next: (course) => (this.course = course),
      error: () => (this.error = 'Не удалось загрузить курс')
    });
  }

  addModule(): void {
    this.courseService.createModule(this.courseId, this.moduleForm).subscribe({
      next: (module) => {
        if (this.course) {
          this.course.modules = [...(this.course.modules || []), module];
        }
        this.message = 'Модуль добавлен';
      },
      error: () => (this.error = 'Ошибка создания модуля')
    });
  }

  addLesson(): void {
    if (!this.lessonForm.moduleId) {
      this.error = 'Выберите модуль для урока';
      return;
    }
    const {moduleId, ...payload} = this.lessonForm;
    this.courseService.createLesson(this.courseId, moduleId, payload).subscribe({
      next: (lesson) => {
        const target = this.course?.modules?.find((m) => m.id === moduleId);
        if (target) {
          target.lessons = [...(target.lessons || []), lesson as Lesson];
        }
        this.message = 'Урок добавлен';
      },
      error: () => (this.error = 'Ошибка создания урока')
    });
  }

  enroll(): void {
    this.courseService.enroll(this.courseId, {studentId: this.enrollStudentId}).subscribe({
      next: () => (this.message = 'Студент зачислен'),
      error: () => (this.error = 'Не удалось зачислить студента')
    });
  }

  lessonsCount(module: CourseModule): number {
    return module.lessons?.length || 0;
  }
}
