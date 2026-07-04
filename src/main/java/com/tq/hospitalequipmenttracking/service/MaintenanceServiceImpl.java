package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.CreateMaintenanceRecordRequest;
import com.tq.hospitalequipmenttracking.dto.request.MaintenanceActionRequest;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceRecordResponse;
import com.tq.hospitalequipmenttracking.dto.response.MaintenanceViewResponse;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.*;
import com.tq.hospitalequipmenttracking.repository.EquipmentRepository;
import com.tq.hospitalequipmenttracking.repository.MaintenanceRecordRepository;
import com.tq.hospitalequipmenttracking.repository.PersonRepository;
import com.tq.hospitalequipmenttracking.validation.MaintenanceValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final EquipmentRepository equipmentRepository;
    private final PersonRepository personRepository;

    public MaintenanceServiceImpl(MaintenanceRecordRepository maintenanceRecordRepository, EquipmentRepository equipmentRepository, PersonRepository personRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.equipmentRepository = equipmentRepository;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse createMaintenanceRecord(Long equipmentId, CreateMaintenanceRecordRequest request){
        Equipment equipment = findEquipmentByIdOrThrow(equipmentId);
        Person requestedBy = findPersonByIdOrThrow(request.getRequestedById());

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setEquipment(equipment);
        maintenanceRecord.setRequestedBy(requestedBy);
        maintenanceRecord.setMaintenanceType(request.getMaintenanceType());
        maintenanceRecord.setScheduledDate(request.getScheduledDate());
        maintenanceRecord.setStatus(MaintenanceStatus.SCHEDULED);
        maintenanceRecord.setDescription(request.getDescription());
        maintenanceRecord.setNotes(request.getNotes());
        maintenanceRecord.setCreatedAt(LocalDateTime.now());
        maintenanceRecord.setUpdatedAt(LocalDateTime.now());
        MaintenanceRecord savedRecord = maintenanceRecordRepository.save( maintenanceRecord);
        return mapToMaintenanceRecordResponse(savedRecord);
    };

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecordResponse> getMaintenanceRecordsByEquipmentId(Long equipmentId){
        findEquipmentByIdOrThrow(equipmentId);
        return maintenanceRecordRepository.findByEquipmentIdOrderByCreatedAtDesc(equipmentId)
                .stream()
                .map(this::mapToMaintenanceRecordResponse)
                .toList();
    };

    @Override
    @Transactional(readOnly = true)
    public MaintenanceRecordResponse getMaintenanceRecordById(Long recordId){
        MaintenanceRecord record = findMaintenanceRecordByIdOrThrow(recordId);
        return mapToMaintenanceRecordResponse(record);
    };

    @Override
    @Transactional
    public MaintenanceRecordResponse startMaintenance(Long recordId, MaintenanceActionRequest request){
        MaintenanceRecord record = findMaintenanceRecordByIdOrThrow(recordId);

        String performedBy = request != null ? request.getPerformedBy() : null;
        String notes = request != null ? request.getNotes() : null;

        MaintenanceValidator.validateCanStart(record);
        record.setStatus(MaintenanceStatus.IN_PROGRESS);
        record.setPerformedBy(performedBy);
        record.setNotes(notes);
        record.setUpdatedAt(LocalDateTime.now());

        Equipment equipment = record.getEquipment();
        equipment.setStatus(EquipmentStatus.UNDER_MAINTENANCE);

        maintenanceRecordRepository.save(record);
        equipmentRepository.save(equipment);

        return mapToMaintenanceRecordResponse(record);
    };

    @Override
    @Transactional
    public MaintenanceRecordResponse completeMaintenance(Long recordId, MaintenanceActionRequest request){
        MaintenanceRecord record = findMaintenanceRecordByIdOrThrow(recordId);

        MaintenanceValidator.validateCanComplete(record);

        String performedBy = request != null ? request.getPerformedBy() : null;
        String notes = request != null ? request.getNotes() : null;

        record.setStatus(MaintenanceStatus.COMPLETED);
        record.setCompletedDate(LocalDateTime.now());
        record.setPerformedBy(performedBy);
        record.setNotes(notes);
        record.setUpdatedAt(LocalDateTime.now());

        Equipment equipment = record.getEquipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);

        maintenanceRecordRepository.save(record);
        equipmentRepository.save(equipment);

        return mapToMaintenanceRecordResponse(record);
    };

    @Override
    @Transactional
    public MaintenanceRecordResponse cancelMaintenance(Long recordId, MaintenanceActionRequest request){
        MaintenanceRecord record = findMaintenanceRecordByIdOrThrow(recordId);

        MaintenanceValidator.validateCanCancel(record);

        MaintenanceStatus currentStatus = record.getStatus();
        String performedBy = request != null ? request.getPerformedBy() : null;
        String notes = request != null ? request.getNotes() : null;

        record.setStatus(MaintenanceStatus.CANCELED);
        record.setPerformedBy(performedBy);
        record.setNotes(notes);
        record.setUpdatedAt(LocalDateTime.now());
        // 1. maintenance record：CREATED -> SCHEDULED → CANCELED: equipmentStatus: AVAILABLE, no equipmentStatus change
        // 2. maintenance record：CREATED -> SCHEDULED -> start → IN_PROGRESS -> CANCELED: equipmentStatus: UNDER_MAINTENANCE, equipmentStatus changed from UNDER_MAINTENANCE to AVAILABLE
        if (currentStatus == MaintenanceStatus.IN_PROGRESS) {
            Equipment equipment = record.getEquipment();
            equipment.setStatus(EquipmentStatus.AVAILABLE);
            equipmentRepository.save(equipment);
        }

        maintenanceRecordRepository.save(record);

        return mapToMaintenanceRecordResponse(record);
    };

    @Override
    @Transactional
    public MaintenanceViewResponse getMaintenanceView(Long recordId){
        MaintenanceRecord record = findMaintenanceRecordByIdOrThrow(recordId);
        Equipment equipment = record.getEquipment();

        return new MaintenanceViewResponse(
                record.getId(),
                record.getStatus(),
                record.getMaintenanceType(),
                record.getScheduledDate(),
                record.getCompletedDate(),
                record.getPerformedBy(),
                equipment.getId(),
                equipment.getName(),
                equipment.getStatus(),
                record.getRequestedBy().getFirstName() + " " + record.getRequestedBy().getLastName(),
                record.getDescription(),
                record.getNotes()
        );

    }
    /**
     * I separate resource DTOs from view DTOs.
     * MaintenanceRecordResponse represents the maintenance entity itself,
     * while MaintenanceViewResponse combines maintenance and equipment data for frontend convenience.
     * */

    private Equipment findEquipmentByIdOrThrow(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + equipmentId));
    }

    private Person findPersonByIdOrThrow(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + personId));
    }

    private MaintenanceRecord findMaintenanceRecordByIdOrThrow(Long recordId) {
        return maintenanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + recordId));
    }

    private MaintenanceRecordResponse mapToMaintenanceRecordResponse(MaintenanceRecord record) {
        Equipment equipment = record.getEquipment();
        Long equipmentId = equipment != null ? equipment.getId() : null;
        String equipmentName = equipment != null ? equipment.getName() : null;

        Person requestedBy = record.getRequestedBy();
        Long requestedById = requestedBy != null ? requestedBy.getId() : null;
        String requestedByName = requestedBy != null
                ? requestedBy.getFirstName() + " " + requestedBy.getLastName()
                : null;

        return new MaintenanceRecordResponse(
                record.getId(),
                equipmentId,
                equipmentName,
                record.getMaintenanceType(),
                record.getStatus(),
                record.getScheduledDate(),
                record.getCompletedDate(),
                requestedById,
                requestedByName,
                record.getPerformedBy(),
                record.getDescription(),
                record.getNotes(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
