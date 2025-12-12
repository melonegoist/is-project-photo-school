package edu.photo_school.equipment.service;

import edu.photo_school.common.exception.BusinessException;
import edu.photo_school.equipment.domain.Equipment;
import edu.photo_school.equipment.domain.EquipmentBooking;
import edu.photo_school.equipment.domain.EquipmentBookingStatus;
import edu.photo_school.equipment.domain.EquipmentStatus;
import edu.photo_school.equipment.dto.*;
import edu.photo_school.equipment.repository.EquipmentBookingRepository;
import edu.photo_school.equipment.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private static final List<EquipmentBookingStatus> BLOCKING_STATUSES = List.of(
            EquipmentBookingStatus.PENDING,
            EquipmentBookingStatus.CONFIRMED,
            EquipmentBookingStatus.COMPLETED
    );

    private final EquipmentRepository equipmentRepository;
    private final EquipmentBookingRepository equipmentBookingRepository;

    @Transactional
    public EquipmentResponse createEquipment(EquipmentCreateRequest request) {
        Equipment equipment = new Equipment();

        equipment.setName(request.name()); // TODO: builder
        equipment.setDescription(request.description());
        equipment.setStatus(request.status() == null ? EquipmentStatus.AVAILABLE : request.status());
        equipment.setHourlyRate(request.hourlyRate());

        Equipment saved = equipmentRepository.save(equipment);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<EquipmentResponse> listEquipment() {
        return equipmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipmentAvailabilityResponse checkAvailability(Long equipmentId, LocalDateTime from, LocalDateTime to) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new BusinessException("Equipment not found"));

        validateRange(from, to);

        boolean available = equipment.getStatus() == EquipmentStatus.AVAILABLE &&
                equipmentBookingRepository.findByEquipmentIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                        equipmentId,
                        BLOCKING_STATUSES,
                        to,
                        from
                ).isEmpty();

        return new EquipmentAvailabilityResponse(available);
    }

    @Transactional
    public EquipmentBookingResponse bookEquipment(Long equipmentId, EquipmentBookingRequest request) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new BusinessException("Equipment not found"));

        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new BusinessException("Equipment is not available");
        }

        validateRange(request.startTime(), request.endTime());

        boolean hasOverlap = !equipmentBookingRepository
                .findByEquipmentIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                        equipmentId,
                        BLOCKING_STATUSES,
                        request.endTime(),
                        request.startTime()
                ).isEmpty();

        if (hasOverlap) {
            throw new BusinessException("Equipment already booked for this time range");
        }

        EquipmentBooking booking = new EquipmentBooking();

        booking.setEquipment(equipment);
        booking.setUserId(request.userId());
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());

        EquipmentBooking saved = equipmentBookingRepository.save(booking);

        return new EquipmentBookingResponse(
                saved.getId(),
                equipmentId,
                saved.getUserId(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus()
        );
    }

    private void validateRange(LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            throw new BusinessException("Start and end date must be provided");
        }

        if (!from.isBefore(to)) {
            throw new BusinessException("Start date must be before end date");
        }
    }

    private EquipmentResponse toResponse(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getStatus(),
                equipment.getHourlyRate()
        );
    }

}
