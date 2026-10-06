package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.response.DepartmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.RoomResponse;
import com.tq.hospitalequipmenttracking.model.Department;
import com.tq.hospitalequipmenttracking.model.Room;
import com.tq.hospitalequipmenttracking.repository.DepartmentRepository;
import com.tq.hospitalequipmenttracking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;
    private final RoomRepository roomRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, RoomRepository roomRepository) {
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments =  departmentRepository.findAll();
        return mapToDepartmentResponse(departments);
    }

    private List<DepartmentResponse> mapToDepartmentResponse(List<Department> departments) {
        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        for (Department department : departments) {
            Long departmentId = department.getId();
            List<Room> rooms =  roomRepository.findByDepartmentId(departmentId);

            List<RoomResponse> roomResponses = new ArrayList<>();
            for (Room room: rooms) {
                RoomResponse roomResponse = new RoomResponse(room.getId(), room.getName());
                roomResponses.add(roomResponse);
            }

            departmentResponses.add(new DepartmentResponse(departmentId, department.getName(), roomResponses));
        }
        return departmentResponses;
    }
}
