package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment  equipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceType maintenanceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_person_id", nullable = false)
    private Person requestedBy; // internal staff for report issue

    private String performedBy; // this doesn't use Person since it could possible be external vendor

    private String description;
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public Person getRequestedBy() {
        return requestedBy;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public void setRequestedBy(Person requestedBy) {
        this.requestedBy = requestedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
