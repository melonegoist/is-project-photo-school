import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {LessonBooking, ScheduleSlot} from '../models/models';

export interface CreateSlotPayload {
  teacherId: string;
  startTime: string;
  endTime: string;
  lessonId?: string;
  maxStudents?: number;
  priceAmount?: number;
  priceCurrency?: string;
  description?: string;
}

export interface BookSlotPayload {
  studentId: string;
}

@Injectable({providedIn: 'root'})
export class ScheduleService {
  private readonly baseUrl = `${environment.apiUrl}/schedule`;

  constructor(private readonly http: HttpClient) {
  }

  createSlot(payload: CreateSlotPayload): Observable<ScheduleSlot> {
    return this.http.post<ScheduleSlot>(`${this.baseUrl}/slots`, payload);
  }

  getAvailableSlots(teacherId: string, from?: string, to?: string): Observable<ScheduleSlot[]> {
    let params = new HttpParams().set('teacherId', teacherId);
    if (from) params = params.set('from', from);
    if (to) params = params.set('to', to);
    return this.http.get<ScheduleSlot[]>(`${this.baseUrl}/slots`, {params});
  }

  getTeacherSlots(teacherId: string, from?: string, to?: string): Observable<ScheduleSlot[]> {
    let params = new HttpParams();
    if (from) params = params.set('from', from);
    if (to) params = params.set('to', to);
    return this.http.get<ScheduleSlot[]>(`${this.baseUrl}/slots/teacher/${teacherId}`, {params});
  }

  completeSlot(slotId: string, teacherId: string): Observable<void> {
    const params = new HttpParams().set('teacherId', teacherId);
    return this.http.post<void>(`${this.baseUrl}/slots/${slotId}/complete`, null, {params});
  }

  cancelSlot(slotId: string, teacherId: string): Observable<void> {
    const params = new HttpParams().set('teacherId', teacherId);
    return this.http.delete<void>(`${this.baseUrl}/slots/${slotId}`, {params});
  }

  bookSlot(slotId: string, payload: BookSlotPayload): Observable<LessonBooking> {
    return this.http.post<LessonBooking>(`${this.baseUrl}/slots/${slotId}/book`, payload);
  }

  cancelBooking(slotId: string, bookingId: string, studentId: string): Observable<void> {
    const params = new HttpParams().set('studentId', studentId);
    return this.http.delete<void>(`${this.baseUrl}/slots/${slotId}/book/${bookingId}`, {params});
  }

  getSlotBookings(slotId: string): Observable<LessonBooking[]> {
    return this.http.get<LessonBooking[]>(`${this.baseUrl}/slots/${slotId}/book`);
  }
}
