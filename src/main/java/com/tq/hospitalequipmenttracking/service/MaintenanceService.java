package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.CreateMaintenanceRecordRequest;
import com.tq.hospitalequipmenttracking.dto.request.MaintenanceActionRequest;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceRecordResponse;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceViewResponse;

import java.util.List;

public interface MaintenanceService {
    
    MaintenanceRecordResponse createMaintenanceRecord(Long equipmentId, CreateMaintenanceRecordRequest request);

    List<MaintenanceRecordResponse> getMaintenanceRecordsByEquipmentId(Long equipmentId);

    MaintenanceRecordResponse getMaintenanceRecordById(Long recordId);

    MaintenanceRecordResponse startMaintenance(Long recordId, MaintenanceActionRequest request);

    MaintenanceRecordResponse completeMaintenance(Long recordId, MaintenanceActionRequest request);

    MaintenanceRecordResponse cancelMaintenance(Long recordId, MaintenanceActionRequest request);

    MaintenanceViewResponse getMaintenanceView(Long recordId);
}
