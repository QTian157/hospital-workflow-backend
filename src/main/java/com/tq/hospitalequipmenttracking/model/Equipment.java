package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

@Entity
public class Equipment {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    private String location;

    @Enumerated(EnumType.STRING) // 把 enum 以字符串形式存进数据库
    private EquipmentStatus status;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }
}
