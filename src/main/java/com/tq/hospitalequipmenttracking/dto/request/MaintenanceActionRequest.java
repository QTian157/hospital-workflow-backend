package com.tq.hospitalequipmenttracking.dto.request;

import com.tq.hospitalequipmenttracking.model.MaintenanceType;
import jakarta.validation.constraints.NotNull;

public class MaintenanceActionRequest {

    private String notes;
    @NotNull
    private String performedBy;

    public String getNotes() {
        return notes;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
}
