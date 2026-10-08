package com.tq.hospitalequipmenttracking.dto.request;

import com.tq.hospitalequipmenttracking.model.MaintenanceType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateMaintenanceRecordRequest {

    @NotNull
    private Long requestedById;

    @NotNull
    private LocalDateTime scheduledDate;

    @NotNull
    private MaintenanceType maintenanceType;
    private String description;
    private String notes;
    public Long getRequestedById() {
        return requestedById;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public void setRequestedById(Long requestedById) {
        this.requestedById = requestedById;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
