import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {EquipmentAvailabilityResponse, EquipmentBookingResponse, EquipmentResponse} from '../models/models';
import {EquipmentBookingPayload, EquipmentCreatePayload, EquipmentService} from '../services/equipment.service';

@Component({
  standalone: true,
  selector: 'app-equipment',
  imports: [CommonModule, FormsModule],
  templateUrl: './equipment.component.html',
  styleUrl: './equipment.component.scss'
})
export class EquipmentComponent implements OnInit {
  equipment: EquipmentResponse[] = [];
  equipmentForm: EquipmentCreatePayload = {name: '', description: '', status: 'AVAILABLE', hourlyRate: 0};
  availabilityForm = {id: 0, from: '', to: ''};
  bookingForm: EquipmentBookingPayload & { id?: number } = {id: undefined, userId: '', startTime: '', endTime: ''};
  availability?: EquipmentAvailabilityResponse;
  booking?: EquipmentBookingResponse;
  message?: string;
  error?: string;

  constructor(private readonly equipmentService: EquipmentService) {
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.equipmentService.list().subscribe({
      next: (data) => (this.equipment = data),
      error: () => (this.error = 'Не удалось загрузить оборудование')
    });
  }

  create(): void {
    this.equipmentService.create(this.equipmentForm).subscribe({
      next: (item) => {
        this.equipment = [...this.equipment, item];
        this.message = 'Оборудование создано';
      },
      error: () => (this.error = 'Не удалось создать оборудование')
    });
  }

  check(): void {
    this.availability = undefined;
    this.equipmentService
      .checkAvailability(this.availabilityForm.id, new Date(this.availabilityForm.from).toISOString(), new Date(this.availabilityForm.to).toISOString())
      .subscribe({
        next: (availability) => (this.availability = availability),
        error: () => (this.error = 'Не удалось проверить доступность')
      });
  }

  book(): void {
    if (!this.bookingForm.id) {
      this.error = 'Укажите оборудование';
      return;
    }
    const payload: EquipmentBookingPayload = {
      userId: this.bookingForm.userId,
      startTime: new Date(this.bookingForm.startTime).toISOString(),
      endTime: new Date(this.bookingForm.endTime).toISOString()
    };
    this.equipmentService.book(this.bookingForm.id, payload).subscribe({
      next: (booking) => {
        this.booking = booking;
        this.message = 'Бронирование создано';
      },
      error: () => (this.error = 'Не удалось создать бронирование')
    });
  }
}
