package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDepartmentId(Long departmentId);
}
