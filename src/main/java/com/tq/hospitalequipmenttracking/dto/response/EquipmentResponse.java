package com.tq.hospitalequipmenttracking.dto.response;
import com.tq.hospitalequipmenttracking.model.*;

public class EquipmentResponse {
    private final Long id;
    private final String name;

    private final EquipmentType type;
    private final EquipmentCategory category;
    private final EquipmentStatus status;

    private final boolean mobile;

    private final String assetTag;
    private final String serialNumber;

    private final Long departmentId;
    private final String departmentName;

    private final Long roomId;
    private final String roomName;


    // ⭐ 构造函数（核心）
    public EquipmentResponse(Long id,
                             String name,
                             EquipmentType type,
                             EquipmentCategory category,
                             EquipmentStatus status,
                             boolean mobile,
                             String assetTag,
                             String serialNumber,
                             Long departmentId,
                             String departmentName,
                             Long roomId,
                             String roomName) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.status = status;
        this.mobile = mobile;
        this.assetTag = assetTag;
        this.serialNumber = serialNumber;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.roomId = roomId;
        this.roomName = roomName;
    }

    // ⭐ 只保留 getter（没有 setter）

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EquipmentType getType() {
        return type;
    }

    public EquipmentCategory getCategory() {
        return category;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public boolean isMobile() {
        return mobile;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
