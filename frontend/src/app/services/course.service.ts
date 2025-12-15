import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Course, CourseModule, Lesson} from '../models/models';

export interface CreateCoursePayload {
  title: string;
  description: string;
  format: 'ONLINE' | 'OFFLINE' | 'HYBRID';
  teacherId: string;
}

export interface CreateModulePayload {
  title: string;
  description: string;
  orderIndex: number;
}

export interface CreateLessonPayload {
  title: string;
  description: string;
  orderIndex: number;
  durationMinutes?: number;
  videoUrl?: string;
  materialsUrl?: string;
  isPreview?: boolean;
}

export interface EnrollPayload {
  studentId: string;
}

@Injectable({providedIn: 'root'})
export class CourseService {
  private readonly baseUrl = `${environment.apiUrl}/courses`;

  constructor(private readonly http: HttpClient) {
  }

  getPublishedCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/published`);
  }

  getCourse(courseId: string): Observable<Course> {
    return this.http.get<Course>(`${this.baseUrl}/${courseId}`);
  }

  createCourse(payload: CreateCoursePayload): Observable<Course> {
    return this.http.post<Course>(this.baseUrl, payload);
  }

  publishCourse(courseId: string): Observable<Course> {
    return this.http.patch<Course>(`${this.baseUrl}/${courseId}/publish`, {});
  }

  unpublishCourse(courseId: string): Observable<Course> {
    return this.http.patch<Course>(`${this.baseUrl}/${courseId}/unpublish`, {});
  }

  createModule(courseId: string, payload: CreateModulePayload): Observable<CourseModule> {
    return this.http.post<CourseModule>(`${this.baseUrl}/${courseId}/modules`, payload);
  }

  updateModule(courseId: string, moduleId: string, payload: CreateModulePayload): Observable<CourseModule> {
    return this.http.put<CourseModule>(`${this.baseUrl}/${courseId}/modules/${moduleId}`, payload);
  }

  deleteModule(courseId: string, moduleId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${courseId}/modules/${moduleId}`);
  }

  createLesson(courseId: string, moduleId: string, payload: CreateLessonPayload): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.baseUrl}/${courseId}/modules/${moduleId}/lessons`, payload);
  }

  updateLesson(courseId: string, moduleId: string, lessonId: string, payload: CreateLessonPayload): Observable<Lesson> {
    return this.http.put<Lesson>(`${this.baseUrl}/${courseId}/modules/${moduleId}/lessons/${lessonId}`, payload);
  }

  deleteLesson(courseId: string, moduleId: string, lessonId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${courseId}/modules/${moduleId}/lessons/${lessonId}`);
  }

  enroll(courseId: string, payload: EnrollPayload) {
    return this.http.post(`${this.baseUrl}/${courseId}/enrollments`, payload);
  }
}
