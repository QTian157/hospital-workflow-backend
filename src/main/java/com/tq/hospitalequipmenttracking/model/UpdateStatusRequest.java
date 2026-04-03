package com.tq.hospitalequipmenttracking.model;

/**
 * 建一个 request DTO: Data Transfer Object
 * 为什么不用直接传 Equipment
     * 因为这次只是改状态，不是整个对象都改。
     * 如果直接传整个 Equipment，很容易让接口含义变得模糊。
 * */
public class UpdateStatusRequest {
    private EquipmentStatus status;

    public EquipmentStatus getStatus() {
        return status;
    }
    public void setStatus(EquipmentStatus status){
        this.status = status;
    }
}
