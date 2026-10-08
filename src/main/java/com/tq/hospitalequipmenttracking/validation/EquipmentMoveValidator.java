package com.tq.hospitalequipmenttracking.validation;

import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.model.*;

import java.util.*;

/**
1. EquipmentType defines what the equipment is.
2. EquipmentCategory groups equipment by functional purpose.
3. Department represents where the equipment is currently located.

These dimensions are intentionally decoupled because a single type of equipment, such as ultrasound, can be used across multiple departments.

 I separated validation into request-level checks, operational constraints, and domain-specific compatibility rules.
 For example, I validate whether a move request is structurally valid, whether the equipment is in a movable status, whether it is physically mobile, and whether its type is compatible with the target department.
 This makes the business logic explicit, testable, and easier to extend.
*/

public final class EquipmentMoveValidator {
    private EquipmentMoveValidator() {};

    private static final Set<EquipmentStatus> MOVE_ALLOWED_STATUSES = EnumSet.of(EquipmentStatus.AVAILABLE, EquipmentStatus.DIRTY);

    private static final Map<EquipmentType, Set<DepartmentType>> ALLOWED_DEPARTMENTS_BY_TYPE = new HashMap<>();

    static {
        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.ULTRASOUND,
                Set.of(DepartmentType.RADIOLOGY, DepartmentType.SURGERY, DepartmentType.CLINIC, DepartmentType.PRE_OP, DepartmentType.ICU));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.INFUSION_PUMP,
                Set.of(DepartmentType.SURGERY, DepartmentType.CLINIC, DepartmentType.PRE_OP, DepartmentType.ICU));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.WHEELCHAIR,
                Set.of(DepartmentType.CLINIC, DepartmentType.PRE_OP, DepartmentType.ICU, DepartmentType.SURGERY, DepartmentType.RADIOLOGY));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.STRETCHER,
                Set.of(DepartmentType.PRE_OP, DepartmentType.SURGERY, DepartmentType.ICU, DepartmentType.RADIOLOGY, DepartmentType.CLINIC));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.PATIENT_MONITOR,
                Set.of(DepartmentType.RADIOLOGY, DepartmentType.SURGERY, DepartmentType.CLINIC, DepartmentType.PRE_OP, DepartmentType.ICU));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.ANESTHESIA_MACHINE,
                Set.of(DepartmentType.SURGERY, DepartmentType.PRE_OP));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.C_ARM,
                Set.of(DepartmentType.SURGERY, DepartmentType.RADIOLOGY));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.CASE_CART,
                Set.of(DepartmentType.SURGERY, DepartmentType.PRE_OP, DepartmentType.MATERIALS_MANAGEMENT, DepartmentType.CSPD));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.AUTOCLAVE, Set.of(DepartmentType.CSPD));

        ALLOWED_DEPARTMENTS_BY_TYPE.put(EquipmentType.WASHER_DISINFECTOR, Set.of(DepartmentType.CSPD));
    }
    public static void validateMoveRequest(Long toRoomId, Long toDepartmentId){
        boolean hasRoom = toRoomId != null;
        boolean hasDepartment = toDepartmentId != null;

        if (hasRoom == hasDepartment) {
            throw new BadRequestException("Exactly one of toRoomId or toDepartmentId must be provided");
        };
    }
    public static void validateTargetRoom(Room toRoom) {
        if (toRoom == null) {
            throw new BadRequestException("Target room is required");
        }
        if (toRoom.getDepartment() == null) {
            throw new BadRequestException("Target room is not associated with a department");
        }
    }

    public static void validateNotSameRoom(Room fromRoom, Room toRoom) {
        if (fromRoom != null && toRoom != null && fromRoom.getId().equals(toRoom.getId())) {
            throw new BadRequestException("Equipment is already in room id " + fromRoom.getId());
        }
    }

    public static void validateNotSameDepartmentWhenNoRoom(Room fromRoom, Department fromDepartment, Department toDepartment) {
        if (fromRoom == null
                && fromDepartment != null
                && toDepartment != null
                && fromDepartment.getId().equals(toDepartment.getId())) {
            throw new BadRequestException("Equipment is already in department id " + fromDepartment.getId());
        }
    }
    // can be moved now?
    public static void validateMoveableStatus(Equipment equipment) {
        if (equipment == null) {
            throw new BadRequestException("Equipment is required");
        }
        if (!MOVE_ALLOWED_STATUSES.contains(equipment.getStatus())) {
            throw new BadRequestException(
                    "Equipment in status " + equipment.getStatus()
                            + " cannot be moved. Allowed statuses: " + MOVE_ALLOWED_STATUSES
            );
        }
    }

    // I also used the mobile flag as an operational constraint.
    // Fixed equipment should not be movable across rooms, while mobile equipment such as wheelchairs or infusion pumps can be relocated.
    public static void validateMobility(Equipment equipment, Room fromRoom, Room toRoom, Department fromDepartment, Department toDepartment) {
        if (equipment == null) {
            throw new BadRequestException("Equipment is required");
        }

        if (equipment.isMobile()) {
            return;
        }

        boolean roomChanged =
                fromRoom != null && toRoom != null && !fromRoom.getId().equals(toRoom.getId());

        boolean departmentChanged =
                fromDepartment != null && toDepartment != null && !fromDepartment.getId().equals(toDepartment.getId());

        if (roomChanged || departmentChanged) {
            throw new BadRequestException("Non-mobile equipment cannot be moved between rooms or departments");
        }
    }

    // can move to?
    public static void validateDepartmentCompatibility(Equipment equipment, Department toDepartment) {
        if (equipment == null) {
            throw new BadRequestException("Equipment is required");
        }
        if (toDepartment == null) {
            throw new BadRequestException("Target department is required");
        }

        Set<DepartmentType> allowedDepartments = ALLOWED_DEPARTMENTS_BY_TYPE.get(equipment.getType());

        if (allowedDepartments != null && !allowedDepartments.contains(toDepartment.getType())) {
            throw new BadRequestException(
                    "Equipment type " + equipment.getType()
                            + " cannot be moved to department " + toDepartment.getType()
                            + ". Allowed departments: " + allowedDepartments
            );
        }
    }
}
