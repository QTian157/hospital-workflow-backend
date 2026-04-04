package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;

    public  Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public DepartmentType getType() {
        return departmentType;
    }
    public void setType(DepartmentType departmentType) {
        this.departmentType = departmentType;
    }
}
