package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.request.*;
import com.tq.hospitalequipmenttracking.dto.response.*;
import com.tq.hospitalequipmenttracking.exception.ApiResponse;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.service.EquipmentService;
import com.tq.hospitalequipmenttracking.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
/**
 * Seperate the response into three layers:
     * "ResponseEntity" handles HTTP concerns such as status codes and headers.
     * "ApiResponse" provides a consistent response contract across APIs
     * "EquipmentResponse" contains the actual business data
 * */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    // 构造函数注入 service layer
    private final EquipmentService equipmentService;
    private final MaintenanceService maintenanceService;

    public EquipmentController(EquipmentService equipmentService, MaintenanceService maintenanceService) {
        this.equipmentService = equipmentService;
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
//    public List<EquipmentResponse> getAllEquipments() {
//        return equipmentService.getAllEquipments();
//    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<List<EquipmentResponse>>> getAllEquipments() {
        return ResponseEntity.ok(
                ApiResponse.success(equipmentService.getAllEquipments())
        );
    }

    @PostMapping
//    public EquipmentResponse addEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
//        System.out.println("===add = ");
//        return equipmentService.addEquipment(request);
//    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>>  addEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Equipment created successfully",
                        equipmentService.addEquipment(request)
                )
        );
    }
    // get - no message
    // create, put, delete - has message

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<List<EquipmentResponse>>> getEquipmentByStatus(@PathVariable EquipmentStatus status) {
        return ResponseEntity.ok(
                ApiResponse.success(equipmentService.getEquipmentByStatus(status))
        );
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
//    public EquipmentResponse getEquipmentById(@PathVariable Long id) {
//        return equipmentService.getEquipmentById(id);
//    }
    public ResponseEntity<ApiResponse<EquipmentResponse>> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(equipmentService.getEquipmentById(id))
        );
    }

    // 这里用DTO:UpdateStatusRequest request
    // Only update basic info: name, type, category, assetTag, serialNumber, mobile. Not touch: room, department, status, assignTo
//    @PutMapping("/{id}/status")
//    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
//    public ResponseEntity<ApiResponse<EquipmentResponse>> updateEquipmentStatus(@PathVariable Long id, @Valid @RequestBody UpdateEquipmentStatusRequest request) {
//        return ResponseEntity.ok(
//                ApiResponse.success(
//                        "Equipment status updated successfully",
//                        equipmentService.updateEquipmentStatus(id,request))
//        );
//    }
    // Only changes for department, room and write MovementHistory
    @PostMapping("/{id}/move")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> moveEquipment(@PathVariable Long id, @Valid @RequestBody MoveEquipmentRequest request) {

//        System.out.println("=== move endpoint id = " + id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Equipment moved successfully",
                        equipmentService.moveEquipment(id, request))
        );
    }

    @GetMapping("/{id}/movement-history")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<List<MovementHistoryResponse>>> getMovementHistoryByEquipmentId(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.success(equipmentService.getMovementHistoryByEquipmentId(id))
        );
    }

    // POST /equipments/{id}/xxx-status-action: check for status legally, change equipment.status, add StatusHistory, some actions for maintenance
    // I used ResponseEntity to have full control over HTTP responses,
    // including status codes and response structure.
    @PostMapping("/{id}/start-use")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> startUse(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.startUse(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Equipment marked as in use successfully", response)
        );
    }

    @PostMapping("/{id}/mark-dirty")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> markDirty(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.markDirty(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Equipment marked as dirty successfully", response)
        );
    }

//    @PostMapping("/{id}/mark-dirty")
//    public EquipmentResponse markDirty(@PathVariable Long id, @Valid @RequestBody StatusActionRequest request) {
//        return equipmentService.markDirty(id, request);
//    }

    @PostMapping("/{id}/start-cleaning")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> startCleaning(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.startCleaning(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Equipment cleaning started successfully",response)
        );
    }

    @PostMapping("/{id}/mark-sterile")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> markSterile(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.markSterile(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Equipment marked as sterile successfully",response)
        );
    }

    @PostMapping("/{id}/return-to-available")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> returnToAvailable(
            @PathVariable Long id,
            @RequestBody(required = false) StatusActionRequest request
    ) {
        EquipmentResponse response = equipmentService.returnToAvailable(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Equipment marked as available successfully",
                        response
                )
        );
    }

//    @PostMapping("/{id}/send-to-maintenance")
//    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
//    public ResponseEntity<ApiResponse<EquipmentResponse>> sendToMaintenance(
//            @PathVariable Long id,
//            @RequestBody(required = false) StatusActionRequest request
//    ) {
//        EquipmentResponse response = equipmentService.sendToMaintenance(id, request);
//        return ResponseEntity.ok(
//                ApiResponse.success(
//                        "Equipment sent to maintenance successfully",
//                        response
//                )
//        );
//    }
//
//    @PostMapping("/{id}/complete-maintenance")
//    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
//    public ResponseEntity<ApiResponse<EquipmentResponse>> completeMaintenance(
//            @PathVariable Long id,
//            @RequestBody(required = false) StatusActionRequest request
//    ) {
//        EquipmentResponse response = equipmentService.completeMaintenance(id, request);
//        return ResponseEntity.ok(
//                ApiResponse.success(
//                        "Equipment maintenance completed successfully",
//                        response
//                )
//        );
//    }

    @GetMapping("/{id}/status-history")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<List<UpdateHistoryResponse>>> getUpdateHistoryByEquipmentId(@PathVariable Long id){
        List<UpdateHistoryResponse> response =
                equipmentService.getUpdateHistoryByEquipmentId(id);
        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> assignEquipment(@PathVariable Long id,
                                             @Valid @RequestBody AssignEquipmentRequest request) {
        EquipmentResponse response = equipmentService.assignEquipment(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Equipment assigned successfully",
                        response
                )
        );
    }

    @PostMapping("/{id}/unassign")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<EquipmentResponse>> unassignEquipment(@PathVariable Long id,
                                               @RequestBody(required = false) AssignmentActionRequest request) {
        EquipmentResponse response = equipmentService.unassignEquipment(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Equipment unassigned successfully",
                        response
                )
        );
    }


    @GetMapping("/{id}/assignment-history")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentHistoryResponse>>> getAssignmentHistory(@PathVariable Long id) {
        List<EquipmentAssignmentHistoryResponse> response =
                equipmentService.getAssignmentHistory(id);

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }


    // maintenance

    @PostMapping("/{equipmentId}/maintenance-records")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<MaintenanceRecordResponse>> createMaintenanceRecord(
            @PathVariable Long equipmentId,
            @Valid @RequestBody CreateMaintenanceRecordRequest request) {
        MaintenanceRecordResponse response =
                maintenanceService.createMaintenanceRecord(equipmentId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Maintenance record created successfully",
                        response
                )
        );
    }

    @GetMapping("/{equipmentId}/maintenance-records")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordResponse>>> getMaintenanceRecordsByEquipmentId(
            @PathVariable Long equipmentId) {
        List<MaintenanceRecordResponse> response =
                maintenanceService.getMaintenanceRecordsByEquipmentId(equipmentId);

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }

    // search
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<PageResponse<EquipmentResponse>>> searchEquipment(
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

        PageResponse<EquipmentResponse> response = new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }

}
