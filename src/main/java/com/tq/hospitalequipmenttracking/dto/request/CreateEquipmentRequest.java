package com.tq.hospitalequipmenttracking.dto.request;

import com.tq.hospitalequipmenttracking.model.*;

/**
 * 需要getter 给request用
 * 需要setter 给Spring Boot用
 */
public class CreateEquipmentRequest {
    private String name;
    private EquipmentType type;
    private EquipmentCategory category;
    private EquipmentStatus status;
    private boolean mobile;

    private Long departmentId;
    private Long roomId;

    private String serialNumber;
    private String assetTag;

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getAssetTag() {
        return assetTag;
    }
    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public EquipmentType getType() {
        return type;
    }
    public void setType(EquipmentType type) {
        this.type = type;
    }
    public EquipmentCategory getCategory() {
        return category;
    }
    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }
    public EquipmentStatus getStatus() {
        return status;
    }
    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }
    public boolean isMobile() {
        return mobile;
    }
    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
