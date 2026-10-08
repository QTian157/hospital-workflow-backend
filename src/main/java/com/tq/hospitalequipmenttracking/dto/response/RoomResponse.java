package com.tq.hospitalequipmenttracking.dto.response;

public class RoomResponse {
    private Long id;
    private String name;

    public RoomResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
