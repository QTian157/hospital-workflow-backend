package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.EquipmentMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementHistoryRepository extends JpaRepository<EquipmentMovementHistory, Long> {
    List<EquipmentMovementHistory> findAllById(Long id);
    List<EquipmentMovementHistory> findAllByIdAndEquipmentId(Long id, Long equipmentId);
    List<EquipmentMovementHistory> findByEquipmentIdOrderByMovedAtDesc(Long equipmentId); // add orderByMovedAt makes list from new to old
}
