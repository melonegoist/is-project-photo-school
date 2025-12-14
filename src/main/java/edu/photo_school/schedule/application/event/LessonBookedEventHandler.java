package edu.photo_school.schedule.application.event;

import edu.photo_school.schedule.domain.events.LessonBookedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LessonBookedEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LessonBookedEventHandler.class);

    // В реальном приложении здесь была бы интеграция с email сервисом, SMS и т.д.
    // private final EmailService emailService;
    // private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleLessonBookedEvent(LessonBookedEvent event) {
        try {
            log.info("Processing lesson booked event: bookingId={}, slotId={}, studentId={}",
                    event.getBookingId(), event.getSlotId(), event.getStudentId());

            // Отправка уведомления студенту
            sendStudentNotification(event.getStudentId(), event.getBookingId());

            // Отправка уведомления преподавателю
            sendTeacherNotification(event.getSlotId(), event.getBookingId());

        } catch (Exception e) {
            log.error("Error handling LessonBookedEvent", e);
        }
    }

    private void sendStudentNotification(UUID studentId, UUID bookingId) {
        // Заглушка для реальной реализации
        log.info("Sending notification to student {} about booking {}", studentId, bookingId);
        // emailService.send(studentEmail, "Booking Confirmed", "Your lesson has been booked");
    }

    private void sendTeacherNotification(UUID slotId, UUID bookingId) {
        // Заглушка для реальной реализации
        log.info("Sending notification to teacher about slot {} and booking {}", slotId, bookingId);
        // emailService.send(teacherEmail, "New Booking", "A student has booked your slot");
    }
}