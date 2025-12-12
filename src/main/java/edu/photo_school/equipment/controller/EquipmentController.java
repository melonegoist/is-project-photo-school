package edu.photo_school.equipment.controller;

import edu.photo_school.equipment.dto.*;
import edu.photo_school.equipment.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public EquipmentResponse createEquipment(@Valid @RequestBody EquipmentCreateRequest request) {
        return equipmentService.createEquipment(request);
    }

    @GetMapping
    public List<EquipmentResponse> listEquipments() {
        return equipmentService.listEquipment();
    }

    @GetMapping("/{id}/availability")
    public EquipmentAvailabilityResponse checkAvailability(
            @PathVariable Long id,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return equipmentService.checkAvailability(id, from, to);
    }

    @PostMapping("/{id}/book")
    public EquipmentBookingResponse bookEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentBookingRequest request
    ) {
        return equipmentService.bookEquipment(id, request);
    }

}
