package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastNmae;
    private String EmployeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonRole role; // NURSE, DOCTOR, TECHNICIAN, STAFF

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
    private boolean active;
}
