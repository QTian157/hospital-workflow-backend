package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.MovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.MarshalledObject;
import java.util.List;

public interface MovementHistoryRepository extends JpaRepository<MovementHistory, Long> {
    List<MovementHistory> findAllById(Long id);
    List<MovementHistory> findAllByIdAndEquipmentId(Long id, Long equipmentId);
    List<MovementHistory> findByEquipmentIdOrderByMovedAtDesc(Long equipmentId); // add orderByMovedAt makes list from new to old
}
