import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  navLinks = [
    { path: '/', label: 'Главная' },
    { path: '/courses', label: 'Курсы' },
    { path: '/schedule', label: 'Расписание' },
    { path: '/equipment', label: 'Оборудование' },
    { path: '/users', label: 'Пользователи' },
    { path: '/auth', label: 'Вход/Регистрация' }
  ];
}
