package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.*;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentAssignmentHistoryResponse;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.MovementHistoryResponse;
import com.tq.hospitalequipmenttracking.dto.response.UpdateHistoryResponse;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;


import java.util.List;

public interface EquipmentService {
    List<EquipmentResponse> getAllEquipments();
    EquipmentResponse addEquipment(CreateEquipmentRequest equipment);
    List<EquipmentResponse> getEquipmentByStatus(EquipmentStatus status);
    EquipmentResponse updateEquipmentStatus(Long id, UpdateEquipmentStatusRequest request);
    EquipmentResponse getEquipmentById(Long id);

    EquipmentResponse moveEquipment(Long equipmentId, MoveEquipmentRequest request);
    List<MovementHistoryResponse> getMovementHistoryByEquipmentId(Long equipmentId);

    EquipmentResponse startUse(Long id, StatusActionRequest request);
    EquipmentResponse markDirty(Long id, StatusActionRequest request);
    EquipmentResponse startCleaning(Long id, StatusActionRequest request);
    EquipmentResponse markSterile(Long id, StatusActionRequest request);
    EquipmentResponse returnToAvailable(Long id, StatusActionRequest request);
    EquipmentResponse sendToMaintenance(Long id, StatusActionRequest request);
    EquipmentResponse completeMaintenance(Long id, StatusActionRequest request);

    List<UpdateHistoryResponse> getUpdateHistoryByEquipmentId(Long equipmentId);

    EquipmentResponse assignEquipment(Long equipmentId, AssignEquipmentRequest request);
    EquipmentResponse unassignEquipment(Long equipmentId, AssignmentActionRequest request);
    List<EquipmentAssignmentHistoryResponse> getAssignmentHistory(Long equipmentId);
}
