package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.request.MaintenanceActionRequest;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceRecordResponse;
import com.tq.hospitalequipmenttracking.model.MaintenanceRecord;
import com.tq.hospitalequipmenttracking.service.MaintenanceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    private final MaintenanceServiceImpl maintenanceServiceImpl;

    public MaintenanceRecordController(MaintenanceServiceImpl maintenanceServiceImpl) {
        this.maintenanceServiceImpl = maintenanceServiceImpl;
    }

    @GetMapping("/{recordId}")
    public MaintenanceRecordResponse getMaintenanceRecordById(@PathVariable Long recordId) {
        return maintenanceServiceImpl.getMaintenanceRecordById(recordId);
    }

    @PostMapping("/{recordId}/start")
    public MaintenanceRecordResponse startMaintenanceRecord(@PathVariable Long recordId, @Valid @RequestBody MaintenanceActionRequest request) {
        return maintenanceServiceImpl.startMaintenance(recordId, request);

    }

    @PostMapping("{recordId}/complete")
    public MaintenanceRecordResponse completeMaintenanceRecord(@PathVariable Long recordId, @Valid @RequestBody MaintenanceActionRequest request) {
        return maintenanceServiceImpl.completeMaintenance(recordId, request);
    }

    @PostMapping("{recordId}/cancle")
    public MaintenanceRecordResponse cancleMaintenanceRecord(@PathVariable Long recordId, @Valid @RequestBody MaintenanceActionRequest request) {
        return maintenanceServiceImpl.cancelMaintenance(recordId, request);
    }
}
