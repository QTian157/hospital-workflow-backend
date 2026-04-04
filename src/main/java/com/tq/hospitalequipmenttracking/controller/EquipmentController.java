package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.request.CreateEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.model.Equipment;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.UpdateStatusRequest;
import com.tq.hospitalequipmenttracking.service.EquipmentService;
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
    public EquipmentResponse addEquipment(@RequestBody CreateEquipmentRequest request) {
        return equipmentService.addEquipment(request);
    }

    @GetMapping("/status/{status}")
    public List<EquipmentResponse> getEquipmentByStatus(@PathVariable EquipmentStatus status) {
        return equipmentService.getEquipmentByStatus(status);
    }

    // 这里用DTO:UpdateStatusRequest request
    @PutMapping("/{id}/status")
    public EquipmentResponse updateEquipmentStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return equipmentService.updateEquipmentStatus(id,request.getStatus());
    }
}
