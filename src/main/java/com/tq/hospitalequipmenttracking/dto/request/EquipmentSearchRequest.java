package com.tq.hospitalequipmenttracking.dto.request;

import com.tq.hospitalequipmenttracking.model.EquipmentCategory;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.EquipmentType;

public class EquipmentSearchRequest {
    private String keyword;
    private EquipmentStatus status;
    private EquipmentType type;
    private EquipmentCategory category;
    private Long departmentId;
    private Long roomId;
    private Boolean mobile;

    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";

    // getters/setters


    public String getKeyword() {
        return keyword;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public EquipmentType getType() {
        return type;
    }

    public EquipmentCategory getCategory() {
        return category;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
}
