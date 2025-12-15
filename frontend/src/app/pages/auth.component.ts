import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {AuthService, LoginPayload, RegisterPayload} from '../services/auth.service';
import {AuthResponse} from '../models/models';

@Component({
  standalone: true,
  selector: 'app-auth',
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {
  authMode: 'login' | 'register' = 'login';
  loginData: LoginPayload = {email: '', password: ''};
  registerData: RegisterPayload = {email: '', password: '', firstName: '', lastName: '', roles: ['STUDENT']};
  rolesInput = 'STUDENT';
  tokens?: AuthResponse;
  message?: string;
  error?: string;

  constructor(private readonly authService: AuthService) {
    const savedAccess = this.authService.accessToken;
    const savedRefresh = this.authService.refreshToken;
    if (savedAccess && savedRefresh) {
      this.tokens = {accessToken: savedAccess, refreshToken: savedRefresh, tokenType: 'Bearer'};
    }
  }

  switchMode(mode: 'login' | 'register'): void {
    this.authMode = mode;
    this.message = undefined;
    this.error = undefined;
  }

  submitLogin(): void {
    this.authService.login(this.loginData).subscribe({
      next: (tokens) => {
        this.tokens = tokens;
        this.message = 'Вход успешен. Токены сохранены в localStorage.';
        this.error = undefined;
      },
      error: () => {
        this.error = 'Ошибка авторизации';
        this.message = undefined;
      }
    });
  }

  submitRegister(): void {
    const payload: RegisterPayload = {
      ...this.registerData,
      roles: this.rolesInput
        ? this.rolesInput.split(',').map((role) => role.trim()).filter(Boolean)
        : ['STUDENT']
    };
    this.authService.register(payload).subscribe({
      next: (tokens) => {
        this.tokens = tokens;
        this.message = 'Регистрация прошла успешно. Токены сохранены в localStorage.';
        this.error = undefined;
      },
      error: () => {
        this.error = 'Не удалось зарегистрироваться';
        this.message = undefined;
      }
    });
  }

  refresh(): void {
    const refreshToken = this.authService.refreshToken;
    if (!refreshToken) {
      this.error = 'Нет refresh токена';
      return;
    }
    this.authService.refresh({refreshToken}).subscribe({
      next: (tokens) => {
        this.tokens = tokens;
        this.message = 'Токен обновлен';
        this.error = undefined;
      },
      error: () => (this.error = 'Не удалось обновить токен')
    });
  }

  logout(): void {
    this.authService.logout();
    this.tokens = undefined;
    this.message = 'Токены очищены';
  }
}
