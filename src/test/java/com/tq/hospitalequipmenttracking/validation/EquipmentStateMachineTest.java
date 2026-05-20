package com.tq.hospitalequipmenttracking.validation;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.StatusAction;
import com.tq.hospitalequipmenttracking.service.EquipmentStateMachine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentStateMachineTest {

    @Test
    void startUse_shouldAllowAvailable() {

        boolean result =
                EquipmentStateMachine
                        .ALLOW_TRANSITIONS
                        .get(StatusAction.START_USE)
                        .contains(EquipmentStatus.AVAILABLE);

        assertTrue(result);
    }

    @Test
    void startUse_shouldRejectDirty() {

        boolean result =
                EquipmentStateMachine
                        .ALLOW_TRANSITIONS
                        .get(StatusAction.START_USE)
                        .contains(EquipmentStatus.DIRTY);

        assertFalse(result);
    }

    @Test
    void startUse_shouldMapToInUse() {

        EquipmentStatus result =
                EquipmentStateMachine
                        .TARGET_STATUS
                        .get(StatusAction.START_USE);

        assertEquals(
                EquipmentStatus.IN_USE,
                result
        );
    }

    @Test
    void completeMaintenance_shouldMapToAvailable() {

        EquipmentStatus result =
                EquipmentStateMachine
                        .TARGET_STATUS
                        .get(StatusAction.COMPLETE_MAINTENANCE);

        assertEquals(
                EquipmentStatus.AVAILABLE,
                result
        );
    }
}