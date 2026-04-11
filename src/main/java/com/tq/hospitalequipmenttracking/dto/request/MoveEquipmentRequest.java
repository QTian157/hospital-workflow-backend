package com.tq.hospitalequipmenttracking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class MoveEquipmentRequest {
//    private Long equipmentId; put in the url not body
    private Long toRoomId;
    private Long toDepartmentId;

    @NotBlank(message = "MovedBy cannot be blank")
    private String movedBy;
    private String notes;

    public Long getToRoomId() {
        return toRoomId;
    }

    public Long getToDepartmentId() {
        return toDepartmentId;
    }

    public String getMovedBy() {
        return movedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setToRoomId(Long toRoomId) {
        this.toRoomId = toRoomId;
    }

    public void setMovedBy(String moveBy) {
        this.movedBy = moveBy;
    }

    public void setNotes(String note) {
        this.notes = note;
    }
}
