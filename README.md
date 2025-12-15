# Backend Architecture Diagrams

This document summarizes the backend structure with Mermaid diagrams for packages, modules (controller-service-repository slices), and core domain classes.

## Package Overview
```mermaid
flowchart LR
    subgraph config[config]
        SecurityConfiguration
        AsyncConfig
        DataInitializer
    end

    subgraph common[common]
        ApiError
        BusinessException
    end

    subgraph auth[auth]
        AuthController
        AuthService
        subgraph security[auth.security]
            JwtTokenProvider
            JwtAuthenticationFilter
            CustomUserDetailsService
        end
    end

    subgraph user[user]
        UserController
        UserService
        UserRepository
        RoleRepository
    end

    subgraph course[course]
        CourseController
        CourseModuleController
        LessonController
        EnrollmentController
        CourseService
        CourseModuleService
        LessonService
        EnrollmentService
        CourseRepository
        CourseModuleRepository
        LessonRepository
        EnrollmentRepository
    end

    subgraph schedule[schedule]
        ScheduleSlotController
        BookingController
        LessonControllerSched[LessonBookingService]
        ScheduleSlotService
        LessonBookingRepository
        ScheduleSlotRepository
    end

    subgraph equipment[equipment]
        EquipmentController
        EquipmentService
        EquipmentRepository
        EquipmentBookingRepository
    end

    config --> auth
    config --> user
    config --> course
    config --> schedule
    config --> equipment

    auth --> user
    course --> schedule
    course --> user
    schedule --> user
    equipment --> user
    equipment --> schedule
    common --> auth
    common --> course
    common --> schedule
    common --> equipment
```

## Module (Layer) Interaction
Each vertical slice follows the same controller → service → repository pattern. The diagram highlights dependencies between slices where they cross boundaries.

```mermaid
flowchart TB
    subgraph Auth
        AC[AuthController] --> AS[AuthService] --> UUSR[UserService]
        AS --> JP[JwtTokenProvider]
    end

    subgraph Users
        UC[UserController] --> USvc[UserService] --> UR[UserRepository]
        USvc --> RR[RoleRepository]
    end

    subgraph Courses
        CCtrl[CourseController] --> CSvc[CourseService] --> CR[CourseRepository]
        MCtrl[CourseModuleController] --> MSvc[CourseModuleService] --> MR[CourseModuleRepository]
        LCtrl[LessonController] --> LSvc[LessonService] --> LR[LessonRepository]
        ECtrl[EnrollmentController] --> ESvc[EnrollmentService] --> ER[EnrollmentRepository]
        CSvc --> USvc
        ESvc --> USvc
    end

    subgraph Schedule
        SSCtrl[ScheduleSlotController] --> SSSvc[ScheduleSlotService] --> SSR[ScheduleSlotRepository]
        BCtrl[BookingController] --> BSvc[LessonBookingService] --> BR[LessonBookingRepository]
        SSSvc --> LSvc
        BSvc --> SSSvc
        BSvc --> USvc
    end

    subgraph Equipment
        EqCtrl[EquipmentController] --> EqSvc[EquipmentService] --> EqR[EquipmentRepository]
        EqSvc --> EqBR[EquipmentBookingRepository]
        EqSvc --> USvc
        EqSvc --> SSSvc
    end
```

## Core Domain Class Relationships
```mermaid
classDiagram
    direction LR
    User --> Role : roles

    Course "1" --> "*" CourseModule : modules
    CourseModule "1" --> "*" Lesson : lessons
    Course "1" --> "*" Enrollment : enrollments by courseId
    Enrollment --> User : studentId

    ScheduleSlot --> Lesson : lessonId
    ScheduleSlot "1" --> "*" LessonBooking : bookings via slotId
    LessonBooking --> User : studentId

    Equipment "1" --> "*" EquipmentBooking : bookings
    EquipmentBooking --> User : userId
```