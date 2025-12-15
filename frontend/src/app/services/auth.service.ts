import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {environment} from '../../environments/environment';
import {AuthResponse} from '../models/models';

export interface RegisterPayload {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RefreshPayload {
  refreshToken: string;
}

@Injectable({providedIn: 'root'})
export class AuthService {
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  constructor(private readonly http: HttpClient) {
  }

  register(payload: RegisterPayload): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, payload).pipe(tap((res) => this.persistTokens(res)));
  }

  login(payload: LoginPayload): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, payload).pipe(tap((res) => this.persistTokens(res)));
  }

  refresh(payload: RefreshPayload): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/refresh`, payload).pipe(tap((res) => this.persistTokens(res)));
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  get accessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  get refreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  get isAuthenticated(): boolean {
    return !!this.accessToken;
  }

  private persistTokens(response: AuthResponse): void {
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
  }
}
