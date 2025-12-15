package edu.photo_school.schedule.application.event;

import edu.photo_school.schedule.domain.events.LessonBookedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonBookedEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LessonBookedEventHandler.class);

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

    private void sendStudentNotification(Long studentId, Long bookingId) {
        // Заглушка для реальной реализации
        log.info("Sending notification to student {} about booking {}", studentId, bookingId);
        // emailService.send(studentEmail, "Booking Confirmed", "Your lesson has been booked");
    }

    private void sendTeacherNotification(Long slotId, Long bookingId) {
        // Заглушка для реальной реализации
        log.info("Sending notification to teacher about slot {} and booking {}", slotId, bookingId);
        // emailService.send(teacherEmail, "New Booking", "A student has booked your slot");
    }

}
