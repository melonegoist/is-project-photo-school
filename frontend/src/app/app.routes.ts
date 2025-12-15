import {Routes} from '@angular/router';
import {HomeComponent} from './pages/home.component';
import {AuthComponent} from './pages/auth.component';
import {CoursesComponent} from './pages/courses.component';
import {CourseDetailComponent} from './pages/course-detail.component';
import {ScheduleComponent} from './pages/schedule.component';
import {EquipmentComponent} from './pages/equipment.component';
import {UsersComponent} from './pages/users.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'auth', component: AuthComponent},
  {path: 'courses', component: CoursesComponent},
  {path: 'courses/:id', component: CourseDetailComponent},
  {path: 'schedule', component: ScheduleComponent},
  {path: 'equipment', component: EquipmentComponent},
  {path: 'users', component: UsersComponent},
  {path: '**', redirectTo: ''}
];
