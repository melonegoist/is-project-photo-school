import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ScheduleService, CreateSlotPayload } from '../services/schedule.service';
import { LessonBooking, ScheduleSlot } from '../models/models';

@Component({
  standalone: true,
  selector: 'app-schedule',
  imports: [CommonModule, FormsModule],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.scss'
})
export class ScheduleComponent {
  filter = { teacherId: '', from: '', to: '' };
  slots: ScheduleSlot[] = [];
  bookings: LessonBooking[] = [];
  bookingStudentId = '';
  slotForm: CreateSlotPayload = {
    teacherId: '',
    startTime: '',
    endTime: '',
    maxStudents: 5,
    priceAmount: 0,
    priceCurrency: 'USD',
    description: ''
  };
  message?: string;
  error?: string;

  constructor(private readonly scheduleService: ScheduleService) {}

  loadAvailable(): void {
    if (!this.filter.teacherId) {
      this.error = 'Укажите ID преподавателя';
      return;
    }
    this.scheduleService
      .getAvailableSlots(this.filter.teacherId, this.toIso(this.filter.from), this.toIso(this.filter.to))
      .subscribe({
        next: (data) => {
          this.slots = data;
          this.error = undefined;
        },
        error: () => (this.error = 'Не удалось загрузить слоты')
      });
  }

  createSlot(): void {
    const payload = { ...this.slotForm, startTime: this.toIso(this.slotForm.startTime), endTime: this.toIso(this.slotForm.endTime) };
    this.scheduleService.createSlot(payload).subscribe({
      next: (slot) => {
        this.slots = [...this.slots, slot];
        this.message = 'Слот создан';
      },
      error: () => (this.error = 'Не удалось создать слот')
    });
  }

  book(slot: ScheduleSlot): void {
    if (!this.bookingStudentId) {
      this.error = 'Укажите студента';
      return;
    }
    this.scheduleService.bookSlot(slot.id, { studentId: this.bookingStudentId }).subscribe({
      next: (booking) => {
        this.message = 'Бронь оформлена';
        this.loadBookings(slot.id);
        if (slot.currentStudents !== undefined) slot.currentStudents += 1;
      },
      error: () => (this.error = 'Не удалось забронировать')
    });
  }

  loadBookings(slotId: string): void {
    this.scheduleService.getSlotBookings(slotId).subscribe({
      next: (bookings) => (this.bookings = bookings),
      error: () => (this.error = 'Не удалось получить бронирования')
    });
  }

  complete(slot: ScheduleSlot): void {
    this.scheduleService.completeSlot(slot.id, slot.teacherId).subscribe({
      next: () => (this.message = 'Слот отмечен как завершенный'),
      error: () => (this.error = 'Не удалось завершить слот')
    });
  }

  cancel(slot: ScheduleSlot): void {
    this.scheduleService.cancelSlot(slot.id, slot.teacherId).subscribe({
      next: () => (this.message = 'Слот отменен'),
      error: () => (this.error = 'Не удалось отменить слот')
    });
  }

  private toIso(value: string): string {
    return value ? new Date(value).toISOString() : '';
  }
}
