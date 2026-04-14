package com.tq.hospitalequipmenttracking.dto.request;

import jakarta.validation.constraints.NotNull;

// pass the equipmentId through the path variable to clearly identify the resource,
// and keep the request body focused only on the action-specific data to avoid redundancy and inconsistency.
public class AssignEquipmentRequest {
    @NotNull(message = "personId cannot be null")
    private Long personId;
    private String notes;

    public Long getPersonId() {
        return personId;
    }

    public String getNotes() {
        return notes;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
