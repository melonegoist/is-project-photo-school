import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {UserResponse} from '../models/models';

export interface CreateUserPayload {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

@Injectable({providedIn: 'root'})
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/users`;

  constructor(private readonly http: HttpClient) {
  }

  create(payload: CreateUserPayload): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.baseUrl, payload);
  }

  list(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.baseUrl);
  }
}
