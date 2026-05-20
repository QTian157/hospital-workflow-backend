package com.tq.hospitalequipmenttracking.validation;

import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * mvn test in Terminal to test all @Test
 * */
class EquipmentStatusTransitionValidatorTest {

    @Test
    void available_shouldBeAbleToChangeToInUse() {
        boolean result = EquipmentStatusTransitionValidator.isValidTransition(
                EquipmentStatus.AVAILABLE,
                EquipmentStatus.IN_USE
        );

        assertTrue(result);
    }

    @Test
    void lost_shouldNotBeAbleToChangeToAvailable() {
        boolean result = EquipmentStatusTransitionValidator.isValidTransition(
                EquipmentStatus.LOST,
                EquipmentStatus.AVAILABLE
        );

        assertFalse(result);
    }

    @Test
    void nullStatus_shouldReturnFalse() {
        boolean result = EquipmentStatusTransitionValidator.isValidTransition(
                null,
                EquipmentStatus.AVAILABLE
        );

        assertFalse(result);
    }
}