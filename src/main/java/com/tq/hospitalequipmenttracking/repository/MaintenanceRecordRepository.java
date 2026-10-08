package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord,Long> {
    List<MaintenanceRecord> findByEquipmentIdOrderByCreatedAtDesc(Long equipmentId);
}
