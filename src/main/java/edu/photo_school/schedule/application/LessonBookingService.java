package edu.photo_school.schedule.application;

import edu.photo_school.schedule.domain.LessonBooking;
import edu.photo_school.schedule.domain.ScheduleSlot;
import edu.photo_school.schedule.infrastructure.repository.LessonBookingRepository;
import edu.photo_school.schedule.infrastructure.repository.ScheduleSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LessonBookingService {

    private static final Logger log = LoggerFactory.getLogger(LessonBookingService.class);

    public LessonBookingService(LessonBookingRepository lessonBookingRepository, ScheduleSlotRepository scheduleSlotRepository, ApplicationEventPublisher eventPublisher) {
        this.lessonBookingRepository = lessonBookingRepository;
        this.scheduleSlotRepository = scheduleSlotRepository;
        this.eventPublisher = eventPublisher;
    }

    private final LessonBookingRepository lessonBookingRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LessonBooking bookSlot(UUID slotId, UUID studentId) {
        // Проверяем существование слота
        ScheduleSlot slot = scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule slot not found"));

        // Проверяем доступность слота
        if (!slot.isAvailable()) {
            throw new IllegalStateException("Slot is not available for booking");
        }

        // Проверяем, не забронировал ли уже студент этот слот
        if (lessonBookingRepository.existsBySlotIdAndStudentId(slotId, studentId)) {
            throw new IllegalStateException("Student already booked this slot");
        }

        // Проверяем количество бронирований
        long currentBookings = lessonBookingRepository.countBySlotId(slotId);
        if (currentBookings >= slot.getMaxStudents()) {
            throw new IllegalStateException("Slot is fully booked");
        }

        // Создаем бронирование
        LessonBooking booking = LessonBooking.create(slotId, studentId);
        booking = lessonBookingRepository.save(booking);

        // Обновляем статус слота
        slot.bookSlot();
        scheduleSlotRepository.save(slot);

        // Публикуем события
        booking.getDomainEvents().forEach(eventPublisher::publishEvent);
        booking.clearDomainEvents();

        log.info("Booked slot: slotId={}, studentId={}, bookingId={}",
                slotId, studentId, booking.getId());

        return booking;
    }

    @Transactional
    public void cancelBooking(UUID bookingId, UUID studentId) {
        LessonBooking booking = lessonBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getStudentId().equals(studentId)) {
            throw new IllegalStateException("Cannot cancel another student's booking");
        }

        booking.cancel();
        lessonBookingRepository.save(booking);

        // Обновляем статус слота
        ScheduleSlot slot = scheduleSlotRepository.findById(booking.getSlotId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule slot not found"));

        slot.cancelBooking();
        scheduleSlotRepository.save(slot);

        log.info("Cancelled booking: bookingId={}, studentId={}", bookingId, studentId);
    }

    @Transactional(readOnly = true)
    public LessonBooking getBooking(UUID bookingId) {
        return lessonBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    @Transactional(readOnly = true)
    public LessonBooking getBookingBySlotAndStudent(UUID slotId, UUID studentId) {
        return lessonBookingRepository.findBySlotIdAndStudentId(slotId, studentId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    @Transactional(readOnly = true)
    public List<LessonBooking> getStudentBookings(UUID studentId) {
        return lessonBookingRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public List<LessonBooking> getSlotBookings(UUID slotId) {
        return lessonBookingRepository.findBySlotId(slotId);
    }
}