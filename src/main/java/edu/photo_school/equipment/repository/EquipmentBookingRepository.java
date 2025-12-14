package edu.photo_school.equipment.repository;

import edu.photo_school.equipment.domain.EquipmentBooking;
import edu.photo_school.equipment.domain.EquipmentBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EquipmentBookingRepository extends JpaRepository<EquipmentBooking, Long> {

    List<EquipmentBooking> findByEquipmentIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long equipmentId,
            List<EquipmentBookingStatus> statuses,
            LocalDateTime start,
            LocalDateTime end
    );

}
