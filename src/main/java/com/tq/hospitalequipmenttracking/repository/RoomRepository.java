package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
