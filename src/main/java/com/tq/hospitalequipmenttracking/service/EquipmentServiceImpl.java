package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.CreateEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.MoveEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.StatusActionRequest;
import com.tq.hospitalequipmenttracking.dto.request.UpdateEquipmentStatusRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.MovementHistoryResponse;
import com.tq.hospitalequipmenttracking.dto.response.UpdateHistoryResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.*;
import com.tq.hospitalequipmenttracking.repository.*;
import com.tq.hospitalequipmenttracking.validation.EquipmentMoveValidator;
import com.tq.hospitalequipmenttracking.validation.EquipmentStatusTransitionValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    // 构造函数注入 repository
    private final EquipmentRepository equipmentRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomRepository roomRepository;

    private final MovementHistoryRepository movementHistoryRepository;
    private final EquipmentStatusHistoryRepository equipmentStatusHistoryRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, DepartmentRepository departmentRepository, RoomRepository roomRepository, MovementHistoryRepository movementHistoryRepository, EquipmentStatusHistoryRepository equipmentStatusHistoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
        this.movementHistoryRepository = movementHistoryRepository;
        this.equipmentStatusHistoryRepository = equipmentStatusHistoryRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentResponse addEquipment(CreateEquipmentRequest request) {
        if (request.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new BadRequestException("New equipment must start with AVAILABLE status");
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException( "Department  not found with id: " + request.getDepartmentId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException( "Room not found with id: " + request.getRoomId()));

        if (room.getDepartment() == null) {
            throw new BadRequestException("Room doesn't belong to any department.");
        };
        // check the consistency of room and department
        if (!room.getDepartment().getId().equals(department.getId())) {
            throw new BadRequestException("Room doesn't belong to the specific department.");
        }

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
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getEquipmentByStatus(EquipmentStatus status) {
        if  (status == null) {throw new BadRequestException("Status cannot be null.");}
        return equipmentRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipmentResponse updateEquipmentStatus(Long id, UpdateEquipmentStatusRequest request){

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        EquipmentStatus curStatus = equipment.getStatus();
        EquipmentStatus newStatus = request.getNewStatus();

        if (!EquipmentStatusTransitionValidator.isValidTransition(curStatus, newStatus)) {
            throw new BadRequestException(
                    "Invalid status transition from " + curStatus
                            + " to " + newStatus
                            + ". Allowed next statuses: "
                            + EquipmentStatusTransitionValidator.getAllowedNextStatuses(curStatus)
            );
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

        // 1. Validate that exactly one target is provided: room or department
        EquipmentMoveValidator.validateMoveRequest(request.getToRoomId(), request.getToDepartmentId());

        Room fromRoom = equipment.getCurrentRoom();
        Department fromDepartment = equipment.getDepartment();

        Room toRoom = null;
        Department toDepartment = null;

        if (request.getToRoomId() != null) {
            toRoom = roomRepository.findById(request.getToRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getToRoomId()));
        // 2. check room correctness/ rooms difference
            EquipmentMoveValidator.validateTargetRoom(toRoom);
            EquipmentMoveValidator.validateNotSameRoom(fromRoom, toRoom);

            toDepartment = toRoom.getDepartment();

        } else {
            toDepartment = departmentRepository.findById(request.getToDepartmentId())
                    .orElseThrow(() -> new  ResourceNotFoundException("Department not found with id: " + request.getToDepartmentId()));

            // 3. For department-only moves, prevent moving to the same department when no room is assigned
            EquipmentMoveValidator.validateNotSameDepartmentWhenNoRoom(fromRoom, fromDepartment, toDepartment);

        }

        // 4. Business rules
        EquipmentMoveValidator.validateMoveableStatus(equipment);
        EquipmentMoveValidator.validateMobility(equipment, fromRoom, toRoom);
        EquipmentMoveValidator.validateDepartmentCompatibility(equipment, toDepartment);

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


    private EquipmentResponse changeStatus( Long equipmentId, StatusAction action, String notes){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentId));

        EquipmentStatus currentStatus = equipment.getStatus();

        Set<EquipmentStatus> allowedStatuses =  EquipmentStateMachine.ALLOW_TRANSITIONS.get(action);
        if (allowedStatuses == null || !allowedStatuses.contains(currentStatus)) {
            throw new BadRequestException("Cannot perform action " + action + " when equipment status is " + currentStatus);
        }
        EquipmentStatus newStatus =  EquipmentStateMachine.TARGET_STATUS.get(action);
        if (newStatus == null) {
            throw new BadRequestException("No target status defined for action: " + action);
        }
        equipment.setStatus(newStatus);
        equipmentRepository.save(equipment);

        EquipmentStatusHistory history = new  EquipmentStatusHistory();
        history.setEquipment(equipment);
        history.setFromStatus(currentStatus);
        history.setToStatus(newStatus);
        history.setAction(action);
        history.setNotes(notes);
        history.setChangedAt(LocalDateTime.now());

        equipmentStatusHistoryRepository.save(history);

        return mapToResponse(equipment);

    }
    @Override
    @Transactional
    public EquipmentResponse startUse(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.START_USE,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    @Transactional
    public EquipmentResponse markDirty(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.MARK_DIRTY,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    @Transactional
    public EquipmentResponse startCleaning(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.START_CLEANING,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    @Transactional
    public EquipmentResponse markSterile(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.MARK_STERILE,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    @Transactional
    public EquipmentResponse returnToAvailable(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.RETURN_TO_AVAILABLE,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    @Transactional
    public EquipmentResponse sendToMaintenance(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.SEND_TO_MAINTENANCE,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    @Transactional
    public EquipmentResponse completeMaintenance(Long id, StatusActionRequest request) {
        return changeStatus(
                id,
                StatusAction.COMPLETE_MAINTENANCE,
                request != null ? request.getNotes() : null
        );
    }

    @Override
    public List<UpdateHistoryResponse> getUpdateHistoryByEquipmentId(Long equipmentId){
        return equipmentStatusHistoryRepository.findByEquipmentIdOrderByChangedAtDesc(equipmentId)
                .stream()
                .map(this::mapToStatusHistoryResponse)
                .toList();

    }

    private UpdateHistoryResponse mapToStatusHistoryResponse(EquipmentStatusHistory history){
        return new UpdateHistoryResponse(
                history.getId(),
                history.getEquipment() != null ? history.getEquipment().getId():null,
                history.getFromStatus()!= null ?  history.getFromStatus():null,
                history.getToStatus() != null ? history.getToStatus():null,
                history.getAction() ,
                history.getNotes(),
                history.getChangedAt()
        );

    }
}
