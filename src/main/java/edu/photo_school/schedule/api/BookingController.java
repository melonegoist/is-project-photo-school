package edu.photo_school.schedule.api;

import edu.photo_school.schedule.api.dto.BookSlotRequest;
import edu.photo_school.schedule.api.dto.LessonBookingResponse;
import edu.photo_school.schedule.application.LessonBookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @PathVariable Long slotId,
            @RequestBody @Valid BookSlotRequest request
    ) {
        return LessonBookingResponse.from(
                lessonBookingService.bookSlot(slotId, request.studentId())
        );
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(
            @PathVariable Long slotId,
            @PathVariable Long bookingId,
            @RequestParam Long studentId
    ) {
        lessonBookingService.cancelBooking(bookingId, studentId);
    }

    @GetMapping("/{bookingId}")
    public LessonBookingResponse getBooking(
            @PathVariable Long slotId,
            @PathVariable Long bookingId
    ) {
        return LessonBookingResponse.from(
                lessonBookingService.getBooking(bookingId)
        );
    }

    @GetMapping
    public List<LessonBookingResponse> getSlotBookings(@PathVariable Long slotId) {
        return lessonBookingService.getSlotBookings(slotId)
                .stream()
                .map(LessonBookingResponse::from)
                .toList();
    }
}