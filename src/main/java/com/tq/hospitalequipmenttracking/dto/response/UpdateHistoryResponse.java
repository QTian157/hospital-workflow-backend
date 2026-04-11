package com.tq.hospitalequipmenttracking.dto.response;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.StatusAction;

import java.time.LocalDateTime;

public class UpdateHistoryResponse {
    private Long id;
    private Long equipmentId;
    private EquipmentStatus fromStatus;
    private EquipmentStatus toStatus;

    private StatusAction action;
    private String notes;
    private LocalDateTime createdAt;

    public UpdateHistoryResponse(Long id, Long equipmentId, EquipmentStatus fromStatus, EquipmentStatus toStatus, StatusAction action, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.action = action;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public EquipmentStatus getFromStatus() {
        return fromStatus;
    }

    public EquipmentStatus getToStatus() {
        return toStatus;
    }

    public StatusAction getAction() {
        return action;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
