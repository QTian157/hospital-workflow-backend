package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @Enumerated(EnumType.STRING)
    private EquipmentCategory category;

    private String assetTag;
    private String serialNumber;

    @Enumerated(EnumType.STRING) // 把 enum 以字符串形式存进数据库
    private EquipmentStatus status;

    private boolean mobile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_room_id")
    private Room currentRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    private LocalDate purchaseDate;
    private LocalDate lastMaintenanceDate;

    // getter & setter
    public Long getId() {
        return id;
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

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
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

    public EquipmentCategory  getCategory() {
        return category;
    }
    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }
    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
    public String getAssetTag() {
        return assetTag;
    }
    public  void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
