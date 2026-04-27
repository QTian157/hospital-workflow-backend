package com.tq.hospitalequipmenttracking.dto.response;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.MaintenanceStatus;
import com.tq.hospitalequipmenttracking.model.MaintenanceType;

import java.time.LocalDateTime;

public class MaintenanceViewResponse {

    // maintenance
    private Long id;
    private MaintenanceStatus maintenanceStatus;
    private MaintenanceType maintenanceType;

    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;

    private String performedBy;

    // equipment
    private Long equipmentId;
    private String equipmentName;
    private EquipmentStatus equipmentStatus;

    // person
    private String requestedByName;

    // others
    private String description;
    private String notes;

    public MaintenanceViewResponse() {}

    public MaintenanceViewResponse(Long id,
                                   MaintenanceStatus maintenanceStatus,
                                   MaintenanceType maintenanceType,
                                   LocalDateTime scheduledDate,
                                   LocalDateTime completedDate,
                                   String performedBy,
                                   Long equipmentId,
                                   String equipmentName,
                                   EquipmentStatus equipmentStatus,
                                   String requestedByName,
                                   String description,
                                   String notes) {
        this.id = id;
        this.maintenanceStatus = maintenanceStatus;
        this.maintenanceType = maintenanceType;
        this.scheduledDate = scheduledDate;
        this.completedDate = completedDate;
        this.performedBy = performedBy;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentStatus = equipmentStatus;
        this.requestedByName = requestedByName;
        this.description = description;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public MaintenanceStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public EquipmentStatus getEquipmentStatus() {
        return equipmentStatus;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }
}