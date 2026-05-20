package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.AssignEquipmentRequest;
import com.tq.hospitalequipmenttracking.dto.request.UpdateEquipmentStatusRequest;
import com.tq.hospitalequipmenttracking.dto.response.EquipmentResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.exception.ResourceNotFoundException;
import com.tq.hospitalequipmenttracking.model.*;
import com.tq.hospitalequipmenttracking.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * If someone check -> dont connect database -> Mockito blocked
 * */
@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository; // fake repository, not connecting database

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MovementHistoryRepository movementHistoryRepository;

    @Mock
    private EquipmentStatusHistoryRepository equipmentStatusHistoryRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private EquipmentAssignmentHistoryRepository equipmentAssignmentHistoryRepository;


    @InjectMocks
    private EquipmentServiceImpl equipmentService; // create EquipmentServiceImpl, inject the fake repository(equipmentRepository)


    @Test
    void getEquipmentById_shouldThrowException_whenEquipmentNotFound() {

        when(equipmentRepository.findById(99L)) // num 99, can't find
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> equipmentService.getEquipmentById(99L)
        );
        // Spring Data JPA return
            // check exist object -> Optional
            // .orElseThrow() -> Optional.empty()
            // check multi -> List<T>
            // Page -> Page<T>
        // Optional.of(...) -> Optional.of(equipment) -> I'm sure the object is not null
        //                  -> Optional.of(null) -> NullPointerException -> Optional.of() not allowed null
        // Optional.empty() -> nothing in the Optional box
        // Optional.ofNullable(...) -> if not null, put in the Optional box
        //                          -> else if is null, return empty()

    }

    @Test
    void getEquipmentById_shouldReturnEquipmentResponse_whenEquipmentExists() {

        Department department = new Department();

        department.setName("Surgery");

        Room room = new Room();

        room.setName("OR-1");
        room.setDepartment(department);

        Equipment equipment = new Equipment();

        equipment.setName("Anesthesia Machine");
        equipment.setType(EquipmentType.ANESTHESIA_MACHINE);
        equipment.setCategory(EquipmentCategory.PROCEDURE);
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipment.setMobile(false);
        equipment.setAssetTag("AT-1001");
        equipment.setSerialNumber("SN-1001");
        equipment.setDepartment(department);
        equipment.setCurrentRoom(room);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        EquipmentResponse response = equipmentService.getEquipmentById(1L);

        assertEquals("Anesthesia Machine", response.getName());
        assertEquals(EquipmentStatus.AVAILABLE, response.getStatus());
        assertEquals("Surgery", response.getDepartmentName());
        assertEquals("OR-1", response.getRoomName());
    }

    // updateEquipmentStatus
    @Test
    void updateEquipmentStatus_shouldThrowException_whenTransitionInvalid() {

        Equipment equipment = new Equipment();

        equipment.setStatus(
                EquipmentStatus.LOST
        );

        UpdateEquipmentStatusRequest request = new UpdateEquipmentStatusRequest();

        request.setNewStatus(EquipmentStatus.AVAILABLE);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        assertThrows(
                BadRequestException.class,
                () -> equipmentService.updateEquipmentStatus(
                        1L,
                        request
                )
        );
    }
    @Test
    void updateEquipmentStatus_shouldUpdateSuccessfully() {

        Equipment equipment = new Equipment();

        equipment.setStatus(
                EquipmentStatus.AVAILABLE
        );

        UpdateEquipmentStatusRequest request =
                new UpdateEquipmentStatusRequest();

        request.setNewStatus(
                EquipmentStatus.IN_USE
        );

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(equipmentRepository.save(any()))
                .thenReturn(equipment);

        EquipmentResponse response =
                equipmentService.updateEquipmentStatus(
                        1L,
                        request
                );

        assertEquals(
                EquipmentStatus.IN_USE,
                response.getStatus()
        );
    }

    // assign
    @Test
    void assignEquipment_shouldAssignSuccessfully() {

        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setActive(true);

        AssignEquipmentRequest request = new AssignEquipmentRequest();
        request.setPersonId(1L);
        request.setNotes("Assigned for surgery");

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(person));

        when(equipmentAssignmentHistoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(equipmentRepository.save(any()))
                .thenReturn(equipment);

        EquipmentResponse response =
                equipmentService.assignEquipment(1L, request);

        assertEquals("John Smith", response.getAssignedPersonName());
        assertNotNull(response.getAssignedAt());

        verify(equipmentAssignmentHistoryRepository).save(any());
        verify(equipmentRepository).save(equipment);
    }

    @Test
    void assignEquipment_shouldThrowException_whenPersonInactive() {

        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);

        Person person = new Person();
        person.setActive(false);

        AssignEquipmentRequest request = new AssignEquipmentRequest();
        request.setPersonId(1L);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(person));

        assertThrows(
                BadRequestException.class,
                () -> equipmentService.assignEquipment(1L, request)
        );

        verify(equipmentRepository, never()).save(any());
        verify(equipmentAssignmentHistoryRepository, never()).save(any());
    }

    @Test
    void assignEquipment_shouldThrowException_whenEquipmentUnderMaintenance() {

        Equipment equipment = new Equipment();

        equipment.setStatus(
                EquipmentStatus.UNDER_MAINTENANCE
        );

        Person person = new Person();

        person.setActive(true);

        AssignEquipmentRequest request =
                new AssignEquipmentRequest();

        request.setPersonId(1L);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(person));

        assertThrows(
                BadRequestException.class,
                () -> equipmentService.assignEquipment(
                        1L,
                        request
                )
        );

        verify(
                equipmentRepository,
                never()
        ).save(any());
    }

    @Test
    void assignEquipment_shouldThrowException_whenAlreadyAssigned() {

        Equipment equipment = new Equipment();

        equipment.setStatus(
                EquipmentStatus.AVAILABLE
        );

        Person existingPerson = new Person();

        existingPerson.setFirstName("Mike");

        equipment.setAssignedPerson(
                existingPerson
        );

        Person person = new Person();

        person.setActive(true);

        AssignEquipmentRequest request =
                new AssignEquipmentRequest();

        request.setPersonId(1L);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(person));

        assertThrows(
                BadRequestException.class,
                () -> equipmentService.assignEquipment(
                        1L,
                        request
                )
        );

        verify(
                equipmentRepository,
                never()
        ).save(any());
    }

}
