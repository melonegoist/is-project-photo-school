import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {Course} from '../models/models';
import {CourseService, CreateCoursePayload} from '../services/course.service';

@Component({
  standalone: true,
  selector: 'app-courses',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];
  loading = false;
  courseForm: CreateCoursePayload = {
    title: '',
    description: '',
    format: 'ONLINE',
    teacherId: ''
  };
  message?: string;
  error?: string;

  constructor(private readonly courseService: CourseService) {
  }

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses(): void {
    this.loading = true;
    this.courseService.getPublishedCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Не удалось загрузить курсы';
        this.loading = false;
      }
    });
  }

  createCourse(): void {
    this.courseService.createCourse(this.courseForm).subscribe({
      next: (course) => {
        this.courses = [...this.courses, course];
        this.message = 'Курс создан';
        this.error = undefined;
      },
      error: () => {
        this.error = 'Ошибка создания курса';
        this.message = undefined;
      }
    });
  }

  publish(course: Course): void {
    this.courseService.publishCourse(course.id).subscribe((updated) => {
      this.updateCourse(updated);
    });
  }

  unpublish(course: Course): void {
    this.courseService.unpublishCourse(course.id).subscribe((updated) => {
      this.updateCourse(updated);
    });
  }

  private updateCourse(updated: Course): void {
    this.courses = this.courses.map((c) => (c.id === updated.id ? updated : c));
  }
}
