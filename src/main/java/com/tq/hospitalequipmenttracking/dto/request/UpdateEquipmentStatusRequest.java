package com.tq.hospitalequipmenttracking.dto.request;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
/**
 * 建一个 request DTO: Data Transfer Object
 * 为什么不用直接传 Equipment
 * 因为这次只是改状态，不是整个对象都改。
 * 如果直接传整个 Equipment，很容易让接口含义变得模糊。
 * */
public class UpdateEquipmentStatusRequest {
    @NotNull(message = "New status is required")
    private EquipmentStatus newStatus;
    @NotBlank(message = "Changing people cannot be null")
    private String changeBy;
    private String note;


    public EquipmentStatus getNewStatus() {
        return newStatus;
    }
    public void setNewStatus(EquipmentStatus status){
        this.newStatus = status;
    }

    public String getChangeBy() {
        return changeBy;
    }

    public String getNote() {
        return note;
    }

    public void setChangeBy(String changeBy) {
        this.changeBy = changeBy;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

