package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.request.*;
import com.tq.hospitalequipmenttracking.dto.response.*;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.service.EquipmentService;
import com.tq.hospitalequipmenttracking.service.MaintenanceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tq.hospitalequipmenttracking.model.EquipmentType;
import com.tq.hospitalequipmenttracking.model.EquipmentCategory;
import org.springframework.data.domain.Page;

import java.util.List;
/**
 * The /api prefix is not required technically,
 * but it is commonly used to distinguish backend endpoints from frontend routes and to support versioning like /api/v1
 * Assign 和 move 是 equipment 的行为，而 maintenance record 是一个独立资源，所以它们的 API 结构不同是合理的。

 * */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    // 构造函数注入 service layer
    private final EquipmentService equipmentService;
    private final MaintenanceServiceImpl maintenanceServiceImpl;

    public EquipmentController(EquipmentService equipmentService, MaintenanceServiceImpl maintenanceServiceImpl) {
        this.equipmentService = equipmentService;
        this.maintenanceServiceImpl = maintenanceServiceImpl;
    }

    @GetMapping
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    @PostMapping
    public EquipmentResponse addEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
        System.out.println("===add = ");
        return equipmentService.addEquipment(request);
    }

    @GetMapping("/status/{status}")
    public List<EquipmentResponse> getEquipmentByStatus(@PathVariable EquipmentStatus status) {
        return equipmentService.getEquipmentByStatus(status);
    }

    @GetMapping("/id/{id}")
    public EquipmentResponse getEquipmentById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id);
    }

    // 这里用DTO:UpdateStatusRequest request
    // Only update basic info: name, type, category, assetTag, serialNumber, mobile. Not touch: room, department, status, assignTo
    @PutMapping("/{id}/status")
    public EquipmentResponse updateEquipmentStatus(@PathVariable Long id, @Valid @RequestBody UpdateEquipmentStatusRequest request) {
        return equipmentService.updateEquipmentStatus(id,request);
    }
    // Only changes for department, room and write MovementHistory
    @PostMapping("/{id}/move")
    public EquipmentResponse moveEquipment(@PathVariable Long id, @Valid @RequestBody MoveEquipmentRequest request) {

//        System.out.println("=== move endpoint id = " + id);
        return equipmentService.moveEquipment(id, request);
    }

    @GetMapping("/{id}/movement-history")
    public List<MovementHistoryResponse> getMovementHistoryByEquipmentId(@PathVariable Long id){
        return equipmentService.getMovementHistoryByEquipmentId(id);
    }

    // POST /equipments/{id}/xxx-status-action: check for status legally, change equipment.status, add StatusHistory, some actions for maintenance
    // I used ResponseEntity to have full control over HTTP responses,
    // including status codes and response structure.
    @PostMapping("/{id}/start-use")
    public ResponseEntity<EquipmentResponse> startUse(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.startUse(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/mark-dirty")
    public ResponseEntity<EquipmentResponse> markDirty(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.markDirty(id, request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/{id}/mark-dirty")
//    public EquipmentResponse markDirty(@PathVariable Long id, @Valid @RequestBody StatusActionRequest request) {
//        return equipmentService.markDirty(id, request);
//    }

    @PostMapping("/{id}/start-cleaning")
    public ResponseEntity<EquipmentResponse> startCleaning(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.startCleaning(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/mark-sterile")
    public ResponseEntity<EquipmentResponse> markSterile(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.markSterile(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/return-to-available")
    public ResponseEntity<EquipmentResponse> returnToAvailable(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.returnToAvailable(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/send-to-maintenance")
    public ResponseEntity<EquipmentResponse> sendToMaintenance(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.sendToMaintenance(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete-maintenance")
    public ResponseEntity<EquipmentResponse> completeMaintenance(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.completeMaintenance(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/status-history")
    public List<UpdateHistoryResponse> getUpdateHistoryByEquipmentId(@PathVariable Long id){
        return equipmentService.getUpdateHistoryByEquipmentId(id);
    }

    @PostMapping("/{id}/assign")
    public EquipmentResponse assignEquipment(@PathVariable Long id,
                                             @Valid @RequestBody AssignEquipmentRequest request) {
        return equipmentService.assignEquipment(id, request);
    }

    @PostMapping("/{id}/unassign")
    public EquipmentResponse unassignEquipment(@PathVariable Long id,
                                               @RequestBody(required = false) AssignmentActionRequest request) {
        return equipmentService.unassignEquipment(id, request);
    }


    @GetMapping("/{id}/assignment-history")
    public List<EquipmentAssignmentHistoryResponse> getAssignmentHistory(@PathVariable Long id) {
        return equipmentService.getAssignmentHistory(id);
    }


    // maintenance

    @PostMapping("/{equipmentId}/maintenance-records")
    public MaintenanceRecordResponse createMaintenanceRecord(
            @PathVariable Long equipmentId,
            @Valid @RequestBody CreateMaintenanceRecordRequest request) {
        return maintenanceServiceImpl.createMaintenanceRecord(equipmentId, request);
    }

    @GetMapping("/{equipmentId}/maintenance-records")
    public List<MaintenanceRecordResponse> getMaintenanceRecordsByEquipmentId(
            @PathVariable Long equipmentId) {
        return maintenanceServiceImpl.getMaintenanceRecordsByEquipmentId(equipmentId);
    }

    // search
    @GetMapping("/search")
    public ResponseEntity<PageResponse<EquipmentResponse>> searchEquipment(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) EquipmentStatus status,
            @RequestParam(required = false) EquipmentType type,
            @RequestParam(required = false) EquipmentCategory category,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Boolean mobile,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        EquipmentSearchRequest request = new EquipmentSearchRequest();
        request.setKeyword(keyword);
        request.setStatus(status);
        request.setType(type);
        request.setCategory(category);
        request.setDepartmentId(departmentId);
        request.setRoomId(roomId);
        request.setMobile(mobile);
        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);

        Page<EquipmentResponse> result = equipmentService.searchEquipment(request);

        // here is PageResponseDTO
        PageResponse<EquipmentResponse> response = new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
        return ResponseEntity.ok(response);
//        return ResponseEntity.ok(result);
    }

}
