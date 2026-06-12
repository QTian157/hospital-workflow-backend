package com.tq.hospitalequipmenttracking.validation;

import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.model.Equipment;
import com.tq.hospitalequipmenttracking.model.EquipmentStatus;
import com.tq.hospitalequipmenttracking.model.MaintenanceRecord;
import com.tq.hospitalequipmenttracking.model.MaintenanceStatus;

import java.util.EnumSet;
import java.util.Set;
/**
 * Start: record -> SCHEDULED + equipment -> AVAILABLE/DIRTY
 * Complete: record -> IN_PROGRESS + equipment -> UNDER_MAINTENANCE
 * Cancel: record -> SCHEDULED or IN_PROGRESS
 * */
public final class MaintenanceValidator {
    private MaintenanceValidator() {};

    private static final Set<EquipmentStatus> MAINTENANCE_START_ALLOWED_STATUSES = EnumSet.of(EquipmentStatus.AVAILABLE, EquipmentStatus.DIRTY);

    public static void validateCanStart(MaintenanceRecord record) {
        validateRecordAndEquipment(record);

        if (record.getStatus() != MaintenanceStatus.SCHEDULED) {
            throw new BadRequestException("Only scheduled maintenance can be started.");
        }
        validateEquipmentCanStartMaintenance(record.getEquipment());
    }

    public static void validateCanComplete(MaintenanceRecord record) {
        validateRecordAndEquipment(record);

        if (record.getStatus() != MaintenanceStatus.IN_PROGRESS) {
            throw new BadRequestException("Only in-progress maintenance can be completed.");
        }
        if (record.getEquipment().getStatus() != EquipmentStatus.UNDER_MAINTENANCE) {
            throw new BadRequestException("Equipment must be UNDER_MAINTENANCE to complete maintenance.");
        }
    }

    public static void validateCanCancel(MaintenanceRecord record) {

        if ( record.getStatus() != MaintenanceStatus.SCHEDULED && record.getStatus() != MaintenanceStatus.IN_PROGRESS) {
            throw new BadRequestException("Only scheduled or in-progress maintenance can be canceled.");
        }
    }

    private static void validateEquipmentCanStartMaintenance(Equipment equipment) {
        if (equipment == null) {
            throw new BadRequestException("Equipment is required.");
        }

        if (!MAINTENANCE_START_ALLOWED_STATUSES.contains(equipment.getStatus())) {
            throw new BadRequestException(
                    "Equipment cannot start maintenance from status: "
                            + equipment.getStatus()
            );
        }
    }
    private static void validateRecordAndEquipment(MaintenanceRecord record) {
        if (record == null) {
            throw new BadRequestException("Maintenance record is required.");
        }

        if (record.getEquipment() == null) {
            throw new BadRequestException("Equipment is required.");
        }
    }
}
