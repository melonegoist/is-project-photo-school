package edu.photo_school.schedule.api;

import edu.photo_school.schedule.api.dto.CreateSlotRequest;
import edu.photo_school.schedule.api.dto.ScheduleSlotResponse;
import edu.photo_school.schedule.application.ScheduleSlotService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule/slots")
public class ScheduleSlotController {

    private final ScheduleSlotService scheduleSlotService;

    public ScheduleSlotController(ScheduleSlotService scheduleSlotService) {
        this.scheduleSlotService = scheduleSlotService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleSlotResponse createSlot(@RequestBody @Valid CreateSlotRequest request) {
        return ScheduleSlotResponse.from(
                scheduleSlotService.createSlot(
                        request.teacherId(),
                        request.startTime(),
                        request.endTime(),
                        request.lessonId(),
                        request.maxStudents(),
                        request.priceAmount(),
                        request.priceCurrency(),
                        request.description()
                )
        );
    }

    @GetMapping
    public List<ScheduleSlotResponse> getAvailableSlots(
            @RequestParam Long teacherId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        return scheduleSlotService.getAvailableSlots(teacherId, from, to)
                .stream()
                .map(ScheduleSlotResponse::from)
                .toList();
    }

    @GetMapping("/teacher/{teacherId}")
    public List<ScheduleSlotResponse> getTeacherSlots(
            @PathVariable Long teacherId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        return scheduleSlotService.getSlotsByTeacher(teacherId, from, to)
                .stream()
                .map(ScheduleSlotResponse::from)
                .toList();
    }

    @PostMapping("/{slotId}/complete")
    @ResponseStatus(HttpStatus.OK)
    public void completeSlot(
            @PathVariable Long slotId,
            @RequestParam Long teacherId
    ) {
        scheduleSlotService.completeSlot(slotId, teacherId);
    }

    @DeleteMapping("/{slotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSlot(
            @PathVariable Long slotId,
            @RequestParam Long teacherId
    ) {
        scheduleSlotService.cancelSlot(slotId, teacherId);
    }
}