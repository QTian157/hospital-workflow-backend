package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.*;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentAssignmentHistoryResponse;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.dto.response.MovementHistoryResponse;
import com.tq.hospitalequipmenttracking.dto.response.UpdateHistoryResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.*;
import com.tq.hospitalequipmenttracking.repository.*;
import com.tq.hospitalequipmenttracking.spec.EquipmentSpecification;
import com.tq.hospitalequipmenttracking.validation.EquipmentMoveValidator;
import com.tq.hospitalequipmenttracking.validation.EquipmentSearchRequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
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

    private final PersonRepository personRepository;
    private final EquipmentAssignmentHistoryRepository equipmentAssignmentHistoryRepository;

    public EquipmentServiceImpl(
            EquipmentRepository equipmentRepository,
            DepartmentRepository departmentRepository,
            RoomRepository roomRepository,
            MovementHistoryRepository movementHistoryRepository,
            EquipmentStatusHistoryRepository equipmentStatusHistoryRepository,
            PersonRepository personRepository,
            EquipmentAssignmentHistoryRepository equipmentAssignmentHistoryRepository)
    {
        this.equipmentRepository = equipmentRepository;
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
        this.movementHistoryRepository = movementHistoryRepository;
        this.equipmentStatusHistoryRepository = equipmentStatusHistoryRepository;
        this.personRepository = personRepository;
        this.equipmentAssignmentHistoryRepository = equipmentAssignmentHistoryRepository;


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

//    @Override
//    @Transactional
//    public EquipmentResponse updateEquipmentStatus(Long id, UpdateEquipmentStatusRequest request){
//
//        Equipment equipment = equipmentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
//
//        EquipmentStatus curStatus = equipment.getStatus();
//        EquipmentStatus newStatus = request.getNewStatus();
//
//        if (!EquipmentStatusTransitionValidator.isValidTransition(curStatus, newStatus)) {
//            throw new BadRequestException(
//                    "Invalid status transition from " + curStatus
//                            + " to " + newStatus
//                            + ". Allowed next statuses: "
//                            + EquipmentStatusTransitionValidator.getAllowedNextStatuses(curStatus)
//            );
//        }
//        equipment.setStatus(newStatus);
//        Equipment updateEquipment = equipmentRepository.save(equipment);
//        return mapToResponse(updateEquipment);
//    }

    @Override
    public EquipmentResponse getEquipmentById(Long id){
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return mapToResponse(equipment);
    }

    private EquipmentResponse mapToResponse(Equipment equipment) {
        Person person = equipment.getAssignedPerson();
        Long personId = person != null ? person.getId() : null;
        String personName = person != null ? person.getFirstName() +" " + person.getLastName() : null;

        Department department = equipment.getDepartment();
        Long departmentId = department != null ? department.getId() : null;
        String departmentName = department != null ? department.getName() : null;

        Room room = equipment.getCurrentRoom();
        Long roomId = room != null ? room.getId() : null;
        String roomName = room != null ? room.getName() : null;

        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getType(),
                equipment.getCategory(),
                equipment.getStatus(),
                equipment.isMobile(),
                equipment.getAssetTag(),
                equipment.getSerialNumber(),
                departmentId, departmentName,
                roomId, roomName,
                personId, personName,
                equipment.getAssignedAt() != null ? equipment.getAssignedAt() : null
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
        EquipmentMoveValidator.validateMobility(equipment, fromRoom, toRoom, fromDepartment, toDepartment);
        EquipmentMoveValidator.validateDepartmentCompatibility(equipment, toDepartment);

        equipment.setCurrentRoom(toRoom);
        equipment.setDepartment(toDepartment);
        Equipment updatedEquipment = equipmentRepository.save(equipment);


        EquipmentMovementHistory equipmentMovementHistory = new EquipmentMovementHistory();
        equipmentMovementHistory.setEquipment(updatedEquipment);
        equipmentMovementHistory.setFromRoom(fromRoom);
        equipmentMovementHistory.setToRoom(toRoom);
        equipmentMovementHistory.setFromDepartment(fromDepartment);
        equipmentMovementHistory.setToDepartment(toDepartment);
        equipmentMovementHistory.setMovedAt(LocalDateTime.now());
        equipmentMovementHistory.setMovedBy(request.getMovedBy());
        equipmentMovementHistory.setNotes(request.getNotes());

        movementHistoryRepository.save(equipmentMovementHistory);

        return mapToResponse(updatedEquipment);
    }

    @Override
    public List<MovementHistoryResponse> getMovementHistoryByEquipmentId(Long equipmentId) {
        return movementHistoryRepository.findByEquipmentIdOrderByMovedAtDesc(equipmentId)
                .stream()
                .map(this::mapToMovementHistoryResponse)
                .toList();

    }
    private MovementHistoryResponse mapToMovementHistoryResponse(EquipmentMovementHistory history) {
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

        if (!EquipmentStateMachine.canPerform(action, currentStatus)) {
            throw new BadRequestException(
                    "Cannot perform action " + action +
                            " when equipment status is " + currentStatus
            );
        }
        EquipmentStatus newStatus = EquipmentStateMachine.getTargetStatus(action);
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

//    @Override
//    @Transactional
//    public EquipmentResponse sendToMaintenance(Long id, StatusActionRequest request) {
//        return changeStatus(
//                id,
//                StatusAction.SEND_TO_MAINTENANCE,
//                request != null ? request.getNotes() : null
//        );
//    }

//    @Override
//    @Transactional
//    public EquipmentResponse completeMaintenance(Long id, StatusActionRequest request) {
//        return changeStatus(
//                id,
//                StatusAction.COMPLETE_MAINTENANCE,
//                request != null ? request.getNotes() : null
//        );
//    }

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

    @Override
    @Transactional
    public EquipmentResponse assignEquipment(Long equipmentId, AssignEquipmentRequest request){
        Equipment equipment = findEquipmentByIDOrThrow(equipmentId);
        Person person = findPersonByIDOrThrow(request.getPersonId());

        validatePersonCanBeAssigned(person);
        validateEquipmentCanBeAssigned(equipment);
        validateEquipmentNotAlreadyAssigned(equipment);

        equipment.setAssignedPerson(person);
        equipment.setAssignedAt(LocalDateTime.now());
        AssignmentAction action = AssignmentAction.ASSIGN;

        String notes = request != null ? request.getNotes() : null;
        EquipmentAssignmentHistory history = buildAssignHistory(equipment, null, person, action, notes);
        equipmentAssignmentHistoryRepository.save(history);
        Equipment saveEquipment = equipmentRepository.save(equipment);
        return mapToResponse(saveEquipment);

    };

    @Override
    @Transactional
    public EquipmentResponse unassignEquipment(Long equipmentId, AssignmentActionRequest request){
        Equipment equipment = findEquipmentByIDOrThrow(equipmentId);
        Person curPerson = equipment.getAssignedPerson();
        if (curPerson == null) {
            throw new BadRequestException("Equipment is not currently assigned.");
        }
        String notes = request != null ? request.getNotes() : null;
        AssignmentAction action = AssignmentAction.UNASSIGN;

        EquipmentAssignmentHistory history = buildAssignHistory(equipment, curPerson, null, action, notes);
        equipmentAssignmentHistoryRepository.save(history);
        equipment.setAssignedPerson(null);
        equipment.setAssignedAt(null);
        Equipment saveEquipment = equipmentRepository.save(equipment);
        return mapToResponse(saveEquipment);

    };

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentHistoryResponse> getAssignmentHistory(Long equipmentId){
        findEquipmentByIDOrThrow(equipmentId);
        return equipmentAssignmentHistoryRepository.findByEquipmentIdOrderByChangedAtDesc(equipmentId)
                .stream()
                .map(this :: mapToAssignmentHistoryResponse)
                .toList();
    };

    private Equipment findEquipmentByIDOrThrow(Long equipmentId){
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id " + equipmentId));
    }
    private Person findPersonByIDOrThrow(Long personId){
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id " + personId));
    }
    private void validatePersonCanBeAssigned(Person person) {
        if (!person.isActive()) {
            throw new BadRequestException("Cannot assign equipment: the selected staff member is inactive or no longer available.");
        }
    };

    // updated
    private static final Set<EquipmentStatus> ASSIGN_ALLOWED_STATUSES =
            EnumSet.of(
                    EquipmentStatus.AVAILABLE,
                    EquipmentStatus.DIRTY,
                    EquipmentStatus.STERILE
            );

    private void validateEquipmentCanBeAssigned(Equipment equipment) {
        if (!ASSIGN_ALLOWED_STATUSES.contains(equipment.getStatus())) {
            throw new BadRequestException(
                    "Equipment cannot be assigned when status is "
                            + equipment.getStatus()
            );
        }
    }

    private void validateEquipmentNotAlreadyAssigned(Equipment equipment){
        if (equipment.getAssignedPerson() != null) {
            throw new  BadRequestException("Equipment is already assigned. Unassign it first.");
        }
    };
    private EquipmentAssignmentHistory buildAssignHistory(Equipment equipment, Person fromPerson, Person toPerson, AssignmentAction action, String notes){
        EquipmentAssignmentHistory history = new EquipmentAssignmentHistory();
        history.setEquipment(equipment);
        history.setFromPerson(fromPerson);
        history.setToPerson(toPerson);
        history.setAction(action);
        history.setNotes(notes);
        history.setChangedAt(LocalDateTime.now());
        return history;
    }
    private EquipmentAssignmentHistoryResponse mapToAssignmentHistoryResponse(EquipmentAssignmentHistory history) {
        Equipment equipment = history.getEquipment();
        Long equipmentId = equipment != null ? equipment.getId() : null;
        String equipmentName = equipment != null ? equipment.getName() : null;

        Person fromPerson = history.getFromPerson();
        Long fromPersonId = fromPerson != null ?  fromPerson.getId() : null;
        String fromPersonName = fromPerson != null ? fromPerson.getFirstName() +" " +  fromPerson.getLastName(): null;

        Person toPerson = history.getToPerson();
        Long toPersonId = toPerson  != null ? toPerson.getId() : null;
        String toPersonName = toPerson != null ? toPerson.getFirstName() +" " +  toPerson.getLastName(): null;

        return new EquipmentAssignmentHistoryResponse(
                history.getId(),
                equipmentId,equipmentName,
                fromPersonId, fromPersonName,
                toPersonId,toPersonName,
                history.getAction() != null ? history.getAction().name() : null,
                history.getNotes() != null ? history.getNotes() : null,
                history.getChangedAt()
        );
    }

    @Override
    public Page<EquipmentResponse> searchEquipment(EquipmentSearchRequest request) {
        // Sorting is not done in memory.
        // We pass the sort configuration through Pageable, and the database applies ORDER BY during query execution

        // 1. Create Sort to define sorting rules (e.g., by name descending)
        // 2. Create Pageable to combine pagination and sorting
        // 3. Build Specification for dynamic filtering conditions
        // 4. Call repository.findAll(spec, pageable)
        // 5. Spring Data JPA generates SQL with WHERE, ORDER BY, LIMIT
        // 6. Database executes query and returns paginated results

        EquipmentSearchRequestValidator.validate(request);

        // 1. sort to give rules: by name by desc
        Sort sort = request.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        // 2. Pagable to put sort rules in it
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        // 3. create SQL and find in database

        return equipmentRepository
                .findAll(EquipmentSpecification.search(request), pageable)
                .map(this::mapToResponse);
    }

}
