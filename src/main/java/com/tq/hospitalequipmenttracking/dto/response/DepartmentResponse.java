package com.tq.hospitalequipmenttracking.dto.response;

import java.util.List;

public class DepartmentResponse {
    private Long id;
    private String name;
    private List<RoomResponse> rooms;

    public DepartmentResponse(Long id, String name, List<RoomResponse> rooms) {
        this.id = id;
        this.name = name;
        this.rooms = rooms;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<RoomResponse> getRooms() {
        return rooms;
    }
}
