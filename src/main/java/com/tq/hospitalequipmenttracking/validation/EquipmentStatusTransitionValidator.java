package com.tq.hospitalequipmenttracking.validation;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


// status lifecycle
public class EquipmentStatusTransitionValidator {
    private static final Map<EquipmentStatus, Set<EquipmentStatus>> ALLOWED_EQUIPMENTS = new HashMap<>();
    static {
        ALLOWED_EQUIPMENTS.put(EquipmentStatus.AVAILABLE,
                Set.of(EquipmentStatus.IN_USE, EquipmentStatus.RESERVED, EquipmentStatus.IN_TRANSIT));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.IN_USE,
                Set.of(EquipmentStatus.DIRTY, EquipmentStatus.IN_TRANSIT));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.DIRTY,
                Set.of(EquipmentStatus.IN_CLEANING));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.IN_CLEANING,
                Set.of(EquipmentStatus.STERILE));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.STERILE,
                Set.of(EquipmentStatus.AVAILABLE));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.RESERVED,
                Set.of(EquipmentStatus.IN_USE, EquipmentStatus.AVAILABLE));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.IN_TRANSIT,
                Set.of(EquipmentStatus.AVAILABLE));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.OUT_OF_SERVICE,
                Set.of(EquipmentStatus.UNDER_MAINTENANCE));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.UNDER_MAINTENANCE,
                Set.of(EquipmentStatus.AVAILABLE));

        ALLOWED_EQUIPMENTS.put(EquipmentStatus.LOST, Set.of());
    }
    private EquipmentStatusTransitionValidator() {};

    public static boolean isValidTransition(EquipmentStatus curStatus, EquipmentStatus newStatus) {
        if (curStatus == null || newStatus == null) {return false;}
        if (curStatus == newStatus) {return true;}
        return ALLOWED_EQUIPMENTS.getOrDefault(curStatus, Set.of()).contains(newStatus);
    }
    public static Set<EquipmentStatus> getAllowedNextStatuses(EquipmentStatus curStatus) {
        if(curStatus == null) {return Set.of();}
        return ALLOWED_EQUIPMENTS.getOrDefault(curStatus, Set.of());
    }

}
