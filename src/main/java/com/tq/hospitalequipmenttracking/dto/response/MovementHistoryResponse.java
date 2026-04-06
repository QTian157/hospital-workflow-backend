package com.tq.hospitalequipmenttracking.dto.response;

import java.time.LocalDateTime;

public class MovementHistoryResponse {
    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private Long fromRoomId;
    private String fromRoomName;
    private Long toRoomId;
    private String toRoomName;
    private Long fromDepartmentId;
    private String fromDepartmentName;
    private Long toDepartmentId;
    private String toDepartmentName;
    private LocalDateTime movedAt;
    private String movedBy;
    private String notes;

    public MovementHistoryResponse(Long id,
                                   Long equipmentId,
                                   String equipmentName,
                                   Long fromRoomId,
                                   String fromRoomName,
                                   Long toRoomId,
                                   String toRoomName,
                                   Long fromDepartmentId,
                                   String fromDepartmentName,
                                   Long toDepartmentId,
                                   String toDepartmentName,
                                   LocalDateTime movedAt,
                                   String movedBy,
                                   String notes) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.fromRoomId = fromRoomId;
        this.fromRoomName = fromRoomName;
        this.toRoomId = toRoomId;
        this.toRoomName = toRoomName;
        this.fromDepartmentId = fromDepartmentId;
        this.fromDepartmentName = fromDepartmentName;
        this.toDepartmentId = toDepartmentId;
        this.toDepartmentName = toDepartmentName;
        this.movedAt = movedAt;
        this.movedBy = movedBy;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Long getFromRoomId() {
        return fromRoomId;
    }

    public String getFromRoomName() {
        return fromRoomName;
    }

    public Long getToRoomId() {
        return toRoomId;
    }

    public String getToRoomName() {
        return toRoomName;
    }

    public Long getFromDepartmentId() {
        return fromDepartmentId;
    }

    public String getFromDepartmentName() {
        return fromDepartmentName;
    }

    public Long getToDepartmentId() {
        return toDepartmentId;
    }

    public String getToDepartmentName() {
        return toDepartmentName;
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


}
