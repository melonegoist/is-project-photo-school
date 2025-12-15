import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Course} from '../models/models';
import {CourseService} from '../services/course.service';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  courses: Course[] = [];
  loading = false;
  error?: string;

  constructor(private readonly courseService: CourseService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.courseService.getPublishedCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Не удалось загрузить опубликованные курсы';
        this.loading = false;
      }
    });
  }
}
