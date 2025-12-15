package edu.photo_school.schedule.application;

import edu.photo_school.schedule.domain.ScheduleSlot;
import edu.photo_school.schedule.infrastructure.repository.ScheduleSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleSlotService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleSlotService.class);

    private final ScheduleSlotRepository scheduleSlotRepository;

    public ScheduleSlotService(ScheduleSlotRepository scheduleSlotRepository) {
        this.scheduleSlotRepository = scheduleSlotRepository;
    }

    @Transactional
    public ScheduleSlot createSlot(
            Long teacherId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Long lessonId,
            Integer maxStudents,
            Double priceAmount,
            String priceCurrency,
            String description
    ) {
        ScheduleSlot slot = ScheduleSlot.create(
                teacherId,
                startTime,
                endTime,
                lessonId,
                maxStudents,
                priceAmount,
                priceCurrency,
                description
        );

        ScheduleSlot savedSlot = scheduleSlotRepository.save(slot);
        log.info("Created schedule slot: id={}, teacherId={}, startTime={}",
                savedSlot.getId(), teacherId, startTime);

        return savedSlot;
    }

    @Transactional(readOnly = true)
    public List<ScheduleSlot> getAvailableSlots(
            Long teacherId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        if (from == null) {
            from = LocalDateTime.now();
        }

        if (to == null) {
            to = from.plusWeeks(2);
        }

        return scheduleSlotRepository.findAvailableSlotsByTeacherIdAndTimeRange(
                teacherId, from, to
        );
    }

    @Transactional(readOnly = true)
    public ScheduleSlot getSlotById(Long slotId) {
        return scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule slot not found"));
    }

    @Transactional(readOnly = true)
    public List<ScheduleSlot> getSlotsByTeacher(Long teacherId, LocalDateTime from, LocalDateTime to) {
        if (from == null && to == null) {
            return scheduleSlotRepository.findByTeacherIdAndStartTimeAfter(teacherId, LocalDateTime.now());
        }

        return scheduleSlotRepository.findByTeacherIdAndStartTimeBetween(teacherId, from, to);
    }

    @Transactional
    public void cancelSlot(Long slotId, Long teacherId) {
        ScheduleSlot slot = scheduleSlotRepository.findByIdAndTeacherId(slotId, teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule slot not found or access denied"));

        slot.markAsCancelled();
        scheduleSlotRepository.save(slot);

        log.info("Cancelled schedule slot: id={}, teacherId={}", slotId, teacherId);
    }

    @Transactional
    public void completeSlot(Long slotId, Long teacherId) {
        ScheduleSlot slot = scheduleSlotRepository.findByIdAndTeacherId(slotId, teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule slot not found or access denied"));

        slot.markAsCompleted();
        scheduleSlotRepository.save(slot);

        log.info("Completed schedule slot: id={}, teacherId={}", slotId, teacherId);
    }
}
