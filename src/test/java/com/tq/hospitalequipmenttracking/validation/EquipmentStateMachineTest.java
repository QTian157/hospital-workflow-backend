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
                EquipmentStateMachine.canPerform(
                        StatusAction.START_USE,
                        EquipmentStatus.AVAILABLE
                );

        assertTrue(result);
    }

    @Test
    void startUse_shouldRejectDirty() {

        boolean result =
                EquipmentStateMachine.canPerform(
                        StatusAction.START_USE,
                        EquipmentStatus.DIRTY
                );

        assertFalse(result);
    }

    @Test
    void startUse_shouldMapToInUse() {

        EquipmentStatus result =
                EquipmentStateMachine
                        .TARGET_STATUSES
                        .get(StatusAction.START_USE);

        assertEquals(
                EquipmentStatus.IN_USE,
                result
        );
    }

    @Test
    void returnToAvailable_shouldMapToAvailable() {

        EquipmentStatus result =
                EquipmentStateMachine.getTargetStatus(
                        StatusAction.RETURN_TO_AVAILABLE
                );

        assertEquals(
                EquipmentStatus.AVAILABLE,
                result
        );
    }
}