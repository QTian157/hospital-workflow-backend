package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.CreateEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.MoveEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.MovementHistoryResponse;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;


import java.util.List;

public interface EquipmentService {
    List<EquipmentResponse> getAllEquipments();
    EquipmentResponse addEquipment(CreateEquipmentRequest equipment);
    List<EquipmentResponse> getEquipmentByStatus(EquipmentStatus status);
    EquipmentResponse updateEquipmentStatus(Long id, EquipmentStatus status);
    EquipmentResponse getEquipmentById(Long id);

    EquipmentResponse moveEquipment(Long equipmentId, MoveEquipmentRequest request);
    List<MovementHistoryResponse> getMovementHistoryByEquipmentId(Long equipmentId);
}
