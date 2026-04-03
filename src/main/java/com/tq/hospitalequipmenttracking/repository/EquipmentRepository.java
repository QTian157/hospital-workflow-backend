package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.Equipment;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//JpaRepository<which entity, primary key>
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // checking based on Status
    List<Equipment> findByStatus(EquipmentStatus status);
}
