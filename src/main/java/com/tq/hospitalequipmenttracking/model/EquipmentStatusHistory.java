package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_status_history")
public class EquipmentStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus fromStatus;

    @Enumerated(EnumType.STRING)

    private EquipmentStatus toStatus;
    // Each status transition is tied to a domain action, which makes the system more traceable and easier to debug.
    @Enumerated(EnumType.STRING)
    private StatusAction action;

    private String notes;
    private LocalDateTime changedAt;

    public Long getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public EquipmentStatus getFromStatus() {
        return fromStatus;
    }

    public EquipmentStatus getToStatus() {
        return toStatus;
    }

    public StatusAction getAction() {
        return action;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setFromStatus(EquipmentStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public void setToStatus(EquipmentStatus toStatus) {
        this.toStatus = toStatus;
    }

    public void setAction(StatusAction action) {
        this.action = action;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
