package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.request.CreateEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.MoveEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.StatusActionRequest;
import com.tq.hospitalequipmenttracking.dto.request.UpdateEquipmentStatusRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.MovementHistoryResponse;
import com.tq.hospitalequipmenttracking.dto.response.UpdateHistoryResponse;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    // 构造函数注入 service layer
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
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

}
