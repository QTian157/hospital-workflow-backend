package com.tq.hospitalequipmenttracking.dto.request;

/**
 * global request can be used for mark-dirt, start-cleaning, mark-sterile, send-to-maintenance
 * */
public class StatusActionRequest {
    String notes;

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
