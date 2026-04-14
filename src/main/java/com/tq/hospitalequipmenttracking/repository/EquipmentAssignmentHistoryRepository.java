package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.EquipmentAssignmentHistory;
import com.tq.hospitalequipmenttracking.model.EquipmentMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentAssignmentHistoryRepository extends JpaRepository<EquipmentAssignmentHistory, Long> {
    List<EquipmentAssignmentHistory> findByEquipmentIdOrderByChangedAtDesc(Long id);
}
