export type CourseFormat = 'ONLINE' | 'OFFLINE' | 'HYBRID';

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
}

export interface UserResponse {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

export interface CourseModule {
  id: string;
  title: string;
  description: string;
  orderIndex: number;
  courseId: string;
  lessons: Lesson[];
}

export interface Course {
  id: string;
  title: string;
  description: string;
  format: CourseFormat;
  published: boolean;
  teacherId: string;
  modules: CourseModule[];
}

export interface Lesson {
  id: string;
  title: string;
  description: string;
  videoUrl?: string;
  materialsUrl?: string;
  durationMinutes?: number;
  orderIndex?: number;
  preview?: boolean;
  isPreview?: boolean;
  moduleId: string;
}

export interface Enrollment {
  id: string;
  courseId: string;
  studentId: string;
  status: string;
}

export interface ScheduleSlot {
  id: string;
  teacherId: string;
  startTime: string;
  endTime: string;
  status: string;
  lessonId?: string;
  maxStudents?: number;
  currentStudents?: number;
  priceAmount?: number;
  priceCurrency?: string;
  description?: string;
}

export interface LessonBooking {
  id: string;
  slotId: string;
  studentId: string;
  status: string;
  createdAt: string;
}

export interface EquipmentResponse {
  id: number;
  name: string;
  description?: string;
  status: string;
  hourlyRate: number;
}

export interface EquipmentAvailabilityResponse {
  available: boolean;
}

export interface EquipmentBookingResponse {
  id: number;
  equipmentId: number;
  userId: string;
  startTime: string;
  endTime: string;
  status: string;
}
