package edu.photo_school.equipment.repository;

import edu.photo_school.equipment.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

}
