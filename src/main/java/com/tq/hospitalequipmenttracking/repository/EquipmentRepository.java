package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.dto.request.EquipmentSearchRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.model.Equipment;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

//JpaRepository<which entity, primary key>
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {

    // checking based on Status
    List<Equipment> findByStatus(EquipmentStatus status);
//    Page<EquipmentResponse> searchEquipment(EquipmentSearchRequest request);
}
