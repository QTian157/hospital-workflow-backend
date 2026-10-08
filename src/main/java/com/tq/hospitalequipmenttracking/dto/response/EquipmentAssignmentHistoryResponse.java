package com.tq.hospitalequipmenttracking.dto.response;

import java.time.LocalDateTime;

public class EquipmentAssignmentHistoryResponse {
    private Long id;
    private Long equipmentId;
    private String equipmentName;;

    private Long fromPersonId;
    private String fromPersonName;

    private Long toPersonId;
    private String toPersonName;

    private String action; // DTO -> String; Entity -> enum
    private String notes;
    private LocalDateTime changedAt;

    public EquipmentAssignmentHistoryResponse() {}

    public EquipmentAssignmentHistoryResponse(
            Long id,
            Long equipmentId,
            String equipmentName,
            Long fromPersonId,
            String fromPersonName,
            Long toPersonId,
            String toPersonName,
            String action,
            String notes,
            LocalDateTime changedAt)
    {
        this.id = id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.fromPersonId = fromPersonId;
        this.fromPersonName = fromPersonName;
        this.toPersonId = toPersonId;
        this.toPersonName = toPersonName;
        this.action = action;
        this.notes = notes;
        this.changedAt = changedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Long getFromPersonId() {
        return fromPersonId;
    }

    public String getFromPersonName() {
        return fromPersonName;
    }

    public Long getToPersonId() {
        return toPersonId;
    }

    public String getToPersonName() {
        return toPersonName;
    }

    public String getAction() {
        return action;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}
