package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.StatusAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EquipmentStateMachine {
    public static final Map<StatusAction, Set<EquipmentStatus>> ALLOW_TRANSITIONS = Map.of(
            StatusAction.START_USE, Set.of(EquipmentStatus.AVAILABLE),
            StatusAction.MARK_DIRTY, Set.of(EquipmentStatus.IN_USE),
            StatusAction.START_CLEANING, Set.of(EquipmentStatus.DIRTY),
            StatusAction.MARK_STERILE, Set.of(EquipmentStatus.IN_CLEANING),
            StatusAction.RETURN_TO_AVAILABLE, Set.of(EquipmentStatus.STERILE),
            StatusAction.SEND_TO_MAINTENANCE, Set.of(
                    EquipmentStatus.AVAILABLE,
                    EquipmentStatus.IN_USE,
                    EquipmentStatus.DIRTY,
                    EquipmentStatus.STERILE),
            StatusAction.COMPLETE_MAINTENANCE, Set.of(EquipmentStatus.UNDER_MAINTENANCE)
    );

    public static final Map<StatusAction, EquipmentStatus> TARGET_STATUS = Map.of(
            StatusAction.START_USE, EquipmentStatus.IN_USE,
            StatusAction.MARK_DIRTY, EquipmentStatus.DIRTY,
            StatusAction.START_CLEANING, EquipmentStatus.IN_CLEANING,
            StatusAction.MARK_STERILE, EquipmentStatus.STERILE,
            StatusAction.RETURN_TO_AVAILABLE, EquipmentStatus.AVAILABLE,
            StatusAction.SEND_TO_MAINTENANCE, EquipmentStatus.UNDER_MAINTENANCE,
            StatusAction.COMPLETE_MAINTENANCE, EquipmentStatus.AVAILABLE
    );

    private EquipmentStateMachine() {}
}
