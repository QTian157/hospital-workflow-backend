package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.EquipmentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentStatusHistoryRepository extends JpaRepository<EquipmentStatusHistory, Long> {
    List<EquipmentStatusHistory> findByEquipmentIdOrderByChangedAtDesc(Long id);
}
