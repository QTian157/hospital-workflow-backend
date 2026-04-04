package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.CreateEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.Department;
import com.tq.hospitalequipmenttracking.model.Equipment;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.Room;
import com.tq.hospitalequipmenttracking.repository.DepartmentRepository;
import com.tq.hospitalequipmenttracking.repository.EquipmentRepository;
import com.tq.hospitalequipmenttracking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    // 构造函数注入 repository
    private final EquipmentRepository equipmentRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomRepository roomRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, DepartmentRepository departmentRepository, RoomRepository roomRepository) {
        this.equipmentRepository = equipmentRepository;
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
    }
    @Override
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentResponse addEquipment(CreateEquipmentRequest request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException( "Department not found with id: " + request.getDepartmentId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException( "Department not found with id: " + request.getRoomId()));

        Equipment equipment = new Equipment();
        equipment.setName(request.getName());
        equipment.setType(request.getType());
        equipment.setCategory(request.getCategory());
        equipment.setStatus(request.getStatus());
        equipment.setMobile(request.isMobile());
        equipment.setSerialNumber(request.getSerialNumber());
        equipment.setAssetTag(request.getAssetTag());
        equipment.setDepartment(department);
        equipment.setCurrentRoom(room);

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return mapToResponse(savedEquipment);

    }
    @Override
    public List<EquipmentResponse> getEquipmentByStatus(EquipmentStatus status) {
        return equipmentRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentResponse updateEquipmentStatus(Long id, EquipmentStatus newStatus){
        if (newStatus == null) {
            throw new BadRequestException("Status cannot be null.");
        }

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        if (equipment.getStatus().equals(newStatus)) {
            throw new BadRequestException("Status is already " + newStatus);
        }
        equipment.setStatus(newStatus);
        Equipment updateEquipment = equipmentRepository.save(equipment);
        return mapToResponse(updateEquipment);
    }

    private EquipmentResponse mapToResponse(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getType(),
                equipment.getCategory(),
                equipment.getStatus(),
                equipment.isMobile(),
                equipment.getAssetTag(),
                equipment.getSerialNumber(),
                equipment.getDepartment() != null ? equipment.getDepartment().getId() : null,
                equipment.getDepartment() != null ? equipment.getDepartment().getName() : null,
                equipment.getCurrentRoom()!= null ? equipment.getCurrentRoom().getId() : null,
                equipment.getCurrentRoom() != null ? equipment.getCurrentRoom().getName() : null
        );
    }

//    private static final Set<String> ALLOWED_STATUSES = Set.of("AVAILABLE", "IN_USE", "CLEANING", "MAINTENANCE");

//    public List<Equipment> getAllEquipments() {
//        return equipmentRepository.findAll();
//    }
//
//    public Equipment addEquipment(Equipment equipment) {
//        return equipmentRepository.save(equipment);
//    }
//
//    public List<Equipment> getEquipmentByStatus(EquipmentStatus status) {
//        return equipmentRepository.findByStatus(status);
//    }
//
//    public Equipment updateStatus(Long id, EquipmentStatus newStatus){
//        if (newStatus == null) {
//            throw new BadRequestException("Status cannot be null.");
//        }
//
//        Equipment equipment = equipmentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
//        if (equipment.getStatus().equals(newStatus)) {
//            throw new BadRequestException("Status is already " + newStatus);
//        }
//        equipment.setStatus(newStatus);
//        return equipmentRepository.save(equipment);
//    }

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
