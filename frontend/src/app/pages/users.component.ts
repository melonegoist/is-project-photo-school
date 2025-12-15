import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, CreateUserPayload } from '../services/user.service';
import { UserResponse } from '../models/models';

@Component({
  standalone: true,
  selector: 'app-users',
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit {
  users: UserResponse[] = [];
  form: CreateUserPayload = { email: '', password: '', firstName: '', lastName: '', roles: ['STUDENT'] };
  rolesInput = 'STUDENT';
  message?: string;
  error?: string;

  constructor(private readonly userService: UserService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.userService.list().subscribe({
      next: (users) => (this.users = users),
      error: () => (this.error = 'Не удалось загрузить пользователей')
    });
  }

  create(): void {
    const payload: CreateUserPayload = {
      ...this.form,
      roles: this.rolesInput
        ? this.rolesInput.split(',').map((role) => role.trim()).filter(Boolean)
        : ['STUDENT']
    };
    this.userService.create(payload).subscribe({
      next: (user) => {
        this.users = [...this.users, user];
        this.message = 'Пользователь создан';
      },
      error: () => (this.error = 'Не удалось создать пользователя')
    });
  }
}
