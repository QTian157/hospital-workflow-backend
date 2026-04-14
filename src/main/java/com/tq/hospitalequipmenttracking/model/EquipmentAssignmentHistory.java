package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_assignment_history")
public class EquipmentAssignmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    // fromPerson / toPerson 必须允许 null
    // assign: from = null, to = personA
    // unassign: from = personA, to = null
    // reassign: from = personA, to = personB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_person_id")
    private Person fromPerson;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_person_id")
    private Person toPerson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentAction action; // ASSIGN, UNASSIGN, REASSIGN

    @Column(length = 500)
    private String notes;

    // @NotNull for application-level validation,
    // @Column(nullable = false) for database-level integrity.
    @Column(nullable = false)
    @NotNull(message = "changed time cannot be null")
    private LocalDateTime changedAt;

    public Long getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Person getFromPerson() {
        return fromPerson;
    }

    public Person getToPerson() {
        return toPerson;
    }

    public AssignmentAction getAction() {
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

    public void setFromPerson(Person fromPerson) {
        this.fromPerson = fromPerson;
    }

    public void setToPerson(Person toPerson) {
        this.toPerson = toPerson;
    }

    public void setAction(AssignmentAction action) {
        this.action = action;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
