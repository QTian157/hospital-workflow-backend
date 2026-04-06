package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 1. create MovementHistory Entity
 * 2. create MoveEquipmentRequest
 * 3. create MoveHistoryRepository
 * 4. add moveEquipment in EquipmentService
 * 5. have move function in EquipmentServiceImpl
 * 6. add controller endpoint
 * 7. Postman test move
 * */

@Entity
@Table(name ="movement_histories")
public class MovementHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_room_id")
    private Room fromRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_room_id")
    private Room toRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_department_id")
    private Department fromDepartment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_department_id")
    private Department toDepartment;

    LocalDateTime movedAt;

    String movedBy;
    String notes;

    public Long getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Room getFromRoom() {
        return fromRoom;
    }

    public Room getToRoom() {
        return toRoom;
    }

    public LocalDateTime getMovedAt() {
        return movedAt;
    }

    public String getMovedBy() {
        return movedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setFromRoom(Room fromRoom) {
        this.fromRoom = fromRoom;
    }

    public void setToRoom(Room toRoom) {
        this.toRoom = toRoom;
    }

    public void setMovedAt(LocalDateTime moveAt) {
        this.movedAt = moveAt;
    }

    public void setMovedBy(String moveBy) {
        this.movedBy = moveBy;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Department getFromDepartment() {
        return fromDepartment;
    }

    public Department getToDepartment() {
        return toDepartment;
    }

    public void setFromDepartment(Department fromDepartment) {
        this.fromDepartment = fromDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }
}
