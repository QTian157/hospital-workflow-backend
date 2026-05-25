package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.request.MaintenanceActionRequest;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceRecordResponse;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceViewResponse;
import com.tq.hospitalequipmenttracking.exception.ApiResponse;
import com.tq.hospitalequipmenttracking.model.MaintenanceRecord;
import com.tq.hospitalequipmenttracking.service.MaintenanceService;
import com.tq.hospitalequipmenttracking.service.MaintenanceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/maintenance-records")
//public class MaintenanceRecordController {
//
//    private final MaintenanceServiceImpl maintenanceServiceImpl;
//
//    public MaintenanceRecordController(MaintenanceServiceImpl maintenanceServiceImpl) {
//        this.maintenanceServiceImpl = maintenanceServiceImpl;
//    }
//
//    @GetMapping("/{recordId}")
//    public MaintenanceRecordResponse getMaintenanceRecordById(@PathVariable Long recordId) {
//        return maintenanceServiceImpl.getMaintenanceRecordById(recordId);
//    }
//
//    @PostMapping("/{recordId}/start")
//    public MaintenanceRecordResponse startMaintenanceRecord(@PathVariable Long recordId, @Valid @RequestBody MaintenanceActionRequest request) {
//        return maintenanceServiceImpl.startMaintenance(recordId, request);
//
//    }
//
//    @PostMapping("{recordId}/complete")
//    public MaintenanceRecordResponse completeMaintenanceRecord(@PathVariable Long recordId, @Valid @RequestBody MaintenanceActionRequest request) {
//        return maintenanceServiceImpl.completeMaintenance(recordId, request);
//    }
//
//    @PostMapping("{recordId}/cancle")
//    public MaintenanceRecordResponse cancleMaintenanceRecord(@PathVariable Long recordId, @Valid @RequestBody MaintenanceActionRequest request) {
//        return maintenanceServiceImpl.cancelMaintenance(recordId, request);
//    }
//
//    @GetMapping("{recordId}/view")
//    public MaintenanceViewResponse getMaintenanceView(@PathVariable Long recordId) {
//        return maintenanceServiceImpl.getMaintenanceView(recordId);
//    }
//}
@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    private final MaintenanceService maintenanceService;

    public MaintenanceRecordController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<MaintenanceRecordResponse>> getMaintenanceRecordById(
            @PathVariable Long recordId) {

        MaintenanceRecordResponse response =
                maintenanceService.getMaintenanceRecordById(recordId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{recordId}/start")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<MaintenanceRecordResponse>> startMaintenanceRecord(
            @PathVariable Long recordId,
            @Valid @RequestBody MaintenanceActionRequest request) {

        MaintenanceRecordResponse response =
                maintenanceService.startMaintenance(recordId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Maintenance started successfully", response)
        );
    }

    @PostMapping("/{recordId}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<MaintenanceRecordResponse>> completeMaintenanceRecord(
            @PathVariable Long recordId,
            @Valid @RequestBody MaintenanceActionRequest request) {

        MaintenanceRecordResponse response =
                maintenanceService.completeMaintenance(recordId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Maintenance completed successfully", response)
        );
    }

    @PostMapping("/{recordId}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<MaintenanceRecordResponse>> cancelMaintenanceRecord(
            @PathVariable Long recordId,
            @Valid @RequestBody MaintenanceActionRequest request) {

        MaintenanceRecordResponse response =
                maintenanceService.cancelMaintenance(recordId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Maintenance cancelled successfully", response)
        );
    }

    @GetMapping("/{recordId}/view")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<MaintenanceViewResponse>> getMaintenanceView(
            @PathVariable Long recordId) {

        MaintenanceViewResponse response =
                maintenanceService.getMaintenanceView(recordId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

// GUEST is read-only, STAFF handles day-to-day operations, ADMIN manages assignment and administrative actions.