import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {EquipmentAvailabilityResponse, EquipmentBookingResponse, EquipmentResponse} from '../models/models';

export interface EquipmentCreatePayload {
  name: string;
  description?: string;
  status?: string;
  hourlyRate: number;
}

export interface EquipmentBookingPayload {
  userId: string;
  startTime: string;
  endTime: string;
}

@Injectable({providedIn: 'root'})
export class EquipmentService {
  private readonly baseUrl = `${environment.apiUrl}/equipment`;

  constructor(private readonly http: HttpClient) {
  }

  list(): Observable<EquipmentResponse[]> {
    return this.http.get<EquipmentResponse[]>(this.baseUrl);
  }

  create(payload: EquipmentCreatePayload): Observable<EquipmentResponse> {
    return this.http.post<EquipmentResponse>(this.baseUrl, payload);
  }

  checkAvailability(id: number, from: string, to: string): Observable<EquipmentAvailabilityResponse> {
    const params = new HttpParams().set('from', from).set('to', to);
    return this.http.get<EquipmentAvailabilityResponse>(`${this.baseUrl}/${id}/availability`, {params});
  }

  book(id: number, payload: EquipmentBookingPayload): Observable<EquipmentBookingResponse> {
    return this.http.post<EquipmentBookingResponse>(`${this.baseUrl}/${id}/book`, payload);
  }
}
