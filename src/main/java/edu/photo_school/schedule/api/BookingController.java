package edu.photo_school.schedule.api;

import edu.photo_school.schedule.api.dto.BookSlotRequest;
import edu.photo_school.schedule.api.dto.LessonBookingResponse;
import edu.photo_school.schedule.application.LessonBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedule/slots/{slotId}/book")
public class BookingController {

    private final LessonBookingService lessonBookingService;

    public BookingController(LessonBookingService lessonBookingService) {
        this.lessonBookingService = lessonBookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonBookingResponse bookSlot(
            @PathVariable UUID slotId,
            @RequestBody @Valid BookSlotRequest request
    ) {
        return LessonBookingResponse.from(
                lessonBookingService.bookSlot(slotId, request.studentId())
        );
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(
            @PathVariable UUID slotId,
            @PathVariable UUID bookingId,
            @RequestParam UUID studentId
    ) {
        lessonBookingService.cancelBooking(bookingId, studentId);
    }

    @GetMapping("/{bookingId}")
    public LessonBookingResponse getBooking(
            @PathVariable UUID slotId,
            @PathVariable UUID bookingId
    ) {
        return LessonBookingResponse.from(
                lessonBookingService.getBooking(bookingId)
        );
    }

    @GetMapping
    public List<LessonBookingResponse> getSlotBookings(@PathVariable UUID slotId) {
        return lessonBookingService.getSlotBookings(slotId)
                .stream()
                .map(LessonBookingResponse::from)
                .toList();
    }
}