package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.Equipment;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.repository.EquipmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EquipmentService {
    // 构造函数注入 repository
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

//    private static final Set<String> ALLOWED_STATUSES = Set.of("AVAILABLE", "IN_USE", "CLEANING", "MAINTENANCE");

    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    public Equipment addEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getEquipmentByStatus(EquipmentStatus status) {
        return equipmentRepository.findByStatus(status);
    }

    public Equipment updateStatus(Long id, EquipmentStatus newStatus){
        if (newStatus == null) {
            throw new BadRequestException("Status cannot be null.");
        }

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        if (equipment.getStatus().equals(newStatus)) {
            throw new BadRequestException("Status is already " + newStatus);
        }
        equipment.setStatus(newStatus);
        return equipmentRepository.save(equipment);
    }

/**
    public Equipment updateStatus(Long id, EquipmentStatus newStatus) {
        if (newStatus == null) {
//            throw new IllegalArgumentException("Status cannot be null.");

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status cannot be null."
            );
        }

//        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
//            throw new IllegalArgumentException("Invalid status: " + newStatus);
//        }


        Equipment equipment = equipmentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));

                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment not found with id: " + id)
                );

        if (equipment.getStatus().equals(newStatus)) {
//            throw new IllegalArgumentException("Status is already" + newStatus);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is already" + newStatus
            );
        }

        equipment.setStatus(newStatus);
        return equipmentRepository.save(equipment);
    }
 */


}
