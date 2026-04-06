package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.CreateEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.MoveEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.MovementHistoryResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.*;
import com.tq.hospitalequipmenttracking.repository.DepartmentRepository;
import com.tq.hospitalequipmenttracking.repository.EquipmentRepository;
import com.tq.hospitalequipmenttracking.repository.MovementHistoryRepository;
import com.tq.hospitalequipmenttracking.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    // 构造函数注入 repository
    private final EquipmentRepository equipmentRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomRepository roomRepository;

    private final MovementHistoryRepository movementHistoryRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, DepartmentRepository departmentRepository, RoomRepository roomRepository, MovementHistoryRepository movementHistoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
        this.movementHistoryRepository = movementHistoryRepository;
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

    @Override
    public EquipmentResponse getEquipmentById(Long id){
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return mapToResponse(equipment);
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

    @Override
    @Transactional
    // 1. move to room, frontend passes "toRoomId"
    // 2. move to department only, frontend pass "toDepartmentId"
    public EquipmentResponse moveEquipment(Long equipmentId, MoveEquipmentRequest request){

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentId));

        // 1.move to room
        boolean hasRoom = request.getToRoomId() != null;

        //2. move to department only
        boolean hasDepartment = request.getToDepartmentId() != null;

        if (hasRoom == hasDepartment) {
            throw new BadRequestException("Exactly one of toRoomId or toDepartmentId must be provided");
        }

        Room fromRoom = equipment.getCurrentRoom();
        Department fromDepartment = equipment.getDepartment();

        Room toRoom = null;
        Department toDepartment = null;
        if (hasRoom) {
            toRoom = roomRepository.findById(request.getToRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getToRoomId()));

            validateRoom(toRoom);

            toDepartment = toRoom.getDepartment();

            if (fromRoom != null && fromRoom.getId().equals(toRoom.getId())) {
                throw new BadRequestException("Equipment is already in room id " + toRoom.getId());
            }
        } else {
            toDepartment = departmentRepository.findById(request.getToDepartmentId())
                    .orElseThrow(() -> new  ResourceNotFoundException("Department not found with id: " + request.getToDepartmentId()));

            toRoom = null;
            if (fromRoom == null &&
                    fromDepartment != null &&
                    fromDepartment.getId().equals(toDepartment.getId())) {
                throw new BadRequestException("Equipment is already in department id " + toDepartment.getId());
            }
        }

        // Bussiness Rule
        validateMoveBusinessRules(equipment, toDepartment);


//        if(toRoom.getDepartment() == null){
//            throw new BadRequestException("Target room is not associate with any department");
//        }


        equipment.setCurrentRoom(toRoom);
        equipment.setDepartment(toDepartment);
        Equipment updatedEquipment = equipmentRepository.save(equipment);


        MovementHistory movementHistory = new MovementHistory();
        movementHistory.setEquipment(updatedEquipment);
        movementHistory.setFromRoom(fromRoom);
        movementHistory.setToRoom(toRoom);
        movementHistory.setFromDepartment(fromDepartment);
        movementHistory.setToDepartment(toDepartment);
        movementHistory.setMovedAt(LocalDateTime.now());
        movementHistory.setMovedBy(request.getMovedBy());
        movementHistory.setNotes(request.getNotes());

        movementHistoryRepository.save(movementHistory);

        return mapToResponse(updatedEquipment);
    }

    private void validateRoom(Room room){
        if(room.getDepartment() == null){
            throw new BadRequestException("Target room is not associated with any department");
        }
    }

    private void validateMoveBusinessRules(Equipment equipment, Department toDepartment){
        if (equipment.getStatus() != EquipmentStatus.AVAILABLE){
            throw new BadRequestException("Only AVAILABLE equipment can be requested to move");
        }
        if (equipment.getCategory() == EquipmentCategory.IMAGING && toDepartment.getType() != DepartmentType.RADIOLOGY) {
            throw new BadRequestException("IMAGING equipment can only be requested to move to RADIOLOGY");
        }
        if (equipment.getCategory() == EquipmentCategory.STERILE_PROCESSING
                && toDepartment.getType() != DepartmentType.CSPD) {
            throw new BadRequestException("STERILE_PROCESSING equipment can only be request to move to CSPD");
        }

    }


    @Override
    public List<MovementHistoryResponse> getMovementHistoryByEquipmentId(Long equipmentId) {
        return movementHistoryRepository.findByEquipmentIdOrderByMovedAtDesc(equipmentId)
                .stream()
                .map(this::mapToMovementHistoryResponse)
                .toList();

    }
    private MovementHistoryResponse mapToMovementHistoryResponse(MovementHistory history) {
        return new MovementHistoryResponse(
                history.getId(),
                history.getEquipment() != null ? history.getEquipment().getId():null,
                history.getEquipment() != null ? history.getEquipment().getName():null,
                history.getFromRoom() != null ? history.getFromRoom().getId():null,
                history.getFromRoom() != null ? history.getFromRoom().getName():null,
                history.getToRoom() != null ? history.getToRoom().getId():null,
                history.getToRoom() != null ? history.getToRoom().getName():null,
                history.getFromDepartment() != null ? history.getFromDepartment().getId() : null,
                history.getFromDepartment() != null ? history.getFromDepartment().getName() : null,
                history.getToDepartment() != null ? history.getToDepartment().getId() : null,
                history.getToDepartment() != null ? history.getToDepartment().getName() : null,
                history.getMovedAt(),
                history.getMovedBy(),
                history.getNotes()
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
