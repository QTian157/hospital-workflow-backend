package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonRole role; // NURSE, DOCTOR, TECHNICIAN, STAFF

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    @Column(nullable = false)
    private boolean active; // staff status: hired (true), unhired (false), temporarily hire (false)

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public PersonRole getRole() {
        return role;
    }

    public Department getDepartment() {
        return department;
    }

    public boolean isActive() {
        return active;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmployeeId(String employeeId) {
        employeeId = employeeId;
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
