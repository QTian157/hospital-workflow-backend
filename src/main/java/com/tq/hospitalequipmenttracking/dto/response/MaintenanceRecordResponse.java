package com.tq.hospitalequipmenttracking.dto.response;

import com.tq.hospitalequipmenttracking.model.MaintenanceStatus;
import com.tq.hospitalequipmenttracking.model.MaintenanceType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceRecordResponse {
    private Long id; // recordId

    private Long equipmentId;
    private String equipmentName;

    private MaintenanceType maintenanceType;
    private MaintenanceStatus status;

    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;

    private Long requestedById;
    private String requestedByName;

    private String performedBy;

    private String description;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public Long getRequestedById() {
        return requestedById;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public MaintenanceRecordResponse() {}

    public MaintenanceRecordResponse(
            Long id,
            Long equipmentId,
            String equipmentName,
            MaintenanceType maintenanceType,
            MaintenanceStatus status,
            LocalDateTime scheduledDate,
            LocalDateTime completedDate,
            Long requestedById,
            String requestedByName,
            String performedBy,
            String description,
            String notes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.maintenanceType = maintenanceType;
        this.status = status;
        this.scheduledDate = scheduledDate;
        this.completedDate = completedDate;
        this.requestedById = requestedById;
        this.requestedByName = requestedByName;
        this.performedBy = performedBy;
        this.description = description;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
