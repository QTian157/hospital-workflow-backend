package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.StatusAction;

import java.util.Map;
import java.util.Set;

public final class EquipmentStateMachine {

    public static final Map<StatusAction, Set<EquipmentStatus>> ALLOWED_CURRENT_STATUSES = Map.of(
            StatusAction.START_USE, Set.of(EquipmentStatus.AVAILABLE),
            StatusAction.MARK_DIRTY, Set.of(EquipmentStatus.IN_USE),
            StatusAction.START_CLEANING, Set.of(EquipmentStatus.DIRTY),
            StatusAction.MARK_STERILE, Set.of(EquipmentStatus.IN_CLEANING),
            StatusAction.RETURN_TO_AVAILABLE, Set.of(EquipmentStatus.STERILE)
    );

    public static final Map<StatusAction, EquipmentStatus> TARGET_STATUSES = Map.of(
            StatusAction.START_USE, EquipmentStatus.IN_USE,
            StatusAction.MARK_DIRTY, EquipmentStatus.DIRTY,
            StatusAction.START_CLEANING, EquipmentStatus.IN_CLEANING,
            StatusAction.MARK_STERILE, EquipmentStatus.STERILE,
            StatusAction.RETURN_TO_AVAILABLE, EquipmentStatus.AVAILABLE
    );

    private EquipmentStateMachine() {}

    public static boolean canPerform(StatusAction action, EquipmentStatus currentStatus) {
        if (action == null || currentStatus == null) {
            return false;
        }

        return ALLOWED_CURRENT_STATUSES
                .getOrDefault(action, Set.of())
                .contains(currentStatus);
    }

    public static EquipmentStatus getTargetStatus(StatusAction action) {
        return TARGET_STATUSES.get(action);
    }
}