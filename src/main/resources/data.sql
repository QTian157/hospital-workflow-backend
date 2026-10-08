
-- =========================
-- Department
-- =========================
INSERT INTO departments (id, name, department_type) VALUES (1, 'Surgery', 'SURGERY');
INSERT INTO departments (id, name, department_type) VALUES (2, 'Imaging', 'RADIOLOGY');
INSERT INTO departments (id, name, department_type) VALUES (3, 'ICU', 'ICU');
INSERT INTO departments (id, name, department_type) VALUES (4, 'CSPD', 'CSPD');
INSERT INTO departments (id, name, department_type) VALUES (5, 'Pre-Op', 'PRE_OP');
INSERT INTO departments (id, name, department_type) VALUES (6, 'Materials Management', 'MATERIALS_MANAGEMENT');

-- =========================
-- Room
-- =========================
INSERT INTO rooms (id, name, department_id) VALUES (1, 'OR-1', 1);
INSERT INTO rooms (id, name, department_id) VALUES (2, 'OR-2', 1);
INSERT INTO rooms (id, name, department_id) VALUES (3, 'CT-1', 2);
INSERT INTO rooms (id, name, department_id) VALUES (4, 'ICU-101', 3);
INSERT INTO rooms (id, name, department_id) VALUES (5, 'Clean Storage', 4);
INSERT INTO rooms (id, name, department_id) VALUES (6, 'Pre-Op Bay 1', 5);
INSERT INTO rooms (id, name, department_id) VALUES (7, 'Supply Room', 6);

-- =========================
-- Person
-- =========================
INSERT INTO people (id, first_name, last_name, employee_id, role, department_id, active)
VALUES (1, 'Emily', 'Chen', 'E1001', 'NURSE', 1, true);

INSERT INTO people (id, first_name, last_name, employee_id, role, department_id, active)
VALUES (2, 'David', 'Li', 'E1002', 'DOCTOR', 1, true);

INSERT INTO people (id, first_name, last_name, employee_id, role, department_id, active)
VALUES (3, 'Sarah', 'Kim', 'E1003', 'TECHNICIAN', 2, true);

INSERT INTO people (id, first_name, last_name, employee_id, role, department_id, active)
VALUES (4, 'Michael', 'Wang', 'E1004', 'BIOMED', 3, true);

INSERT INTO people (id, first_name, last_name, employee_id, role, department_id, active)
VALUES (5, 'Lisa', 'Zhao', 'E1005', 'STAFF', 5, false);

-- ============================================================
-- Equipment - Move Validation Test Data
-- ============================================================
-- Equipment #1-#8 are intentionally configured to test
-- different equipment movement rules.
--
-- Move validation considers:
-- 1. Equipment mobility (mobile / non-mobile)
-- 2. Equipment status
-- 3. Equipment type and allowed destination department
-- 4. Normal movement within or across departments
-- ============================================================


-- ------------------------------------------------------------
-- 1. Anesthesia Machine
-- Initial Location: Surgery / OR-1
-- Status: AVAILABLE
-- Mobile: false
--
-- Test Cases:
-- - OR-1 -> OR-2:
--   REJECTED because the equipment is non-mobile.
--
-- - Surgery -> ICU:
--   REJECTED because the equipment is non-mobile.
--   The destination can also be used to test equipment-type
--   restrictions if anesthesia machines are restricted to Surgery.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Anesthesia Machine', 'ANESTHESIA_MACHINE', 'PROCEDURE',
     'AT-1001', 'SN-1001', 'AVAILABLE', false, 1, 1);


-- ------------------------------------------------------------
-- 2. Ultrasound - In Use
-- Initial Location: Surgery / OR-2
-- Status: IN_USE
-- Mobile: true
--
-- Test Case:
-- - Attempt to move to another room or department:
--   REJECTED because equipment with IN_USE status cannot be moved.
--
-- Purpose:
-- Tests status-based movement validation.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound - In Use', 'ULTRASOUND', 'IMAGING',
     'AT-2001', 'SN-2001', 'IN_USE', true, 2, 1);


-- ------------------------------------------------------------
-- 3. Ultrasound - Available
-- Initial Location: Imaging / CT-1
-- Status: AVAILABLE
-- Mobile: true
--
-- Test Case:
-- - Imaging -> Surgery:
--   ALLOWED.
--
-- Purpose:
-- Tests a valid cross-department movement for mobile,
-- available equipment.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound - Available', 'ULTRASOUND', 'IMAGING',
     'AT-2002', 'SN-2002', 'AVAILABLE', true, 3, 2);


-- ------------------------------------------------------------
-- 4. Infusion Pump
-- Initial Location: ICU / ICU-101
-- Status: AVAILABLE
-- Mobile: true
--
-- Test Cases:
-- - ICU -> Pre-Op:
--   ALLOWED.
--
-- - ICU -> Surgery:
--   ALLOWED.
--
-- Purpose:
-- Tests normal movement of highly mobile equipment between
-- multiple clinical departments.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Infusion Pump', 'INFUSION_PUMP', 'LOGISTICS',
     'AT-4001', 'SN-4001', 'AVAILABLE', true, 4, 3);


-- ------------------------------------------------------------
-- 5. Washer Disinfector
-- Initial Location: CSPD / Clean Storage
-- Status: AVAILABLE
-- Mobile: false
--
-- Test Cases:
-- - Move to another room:
--   REJECTED because the equipment is non-mobile.
--
-- - CSPD -> ICU:
--   REJECTED.
--   This can test both the non-mobile rule and the equipment-type
--   restriction because a washer disinfector belongs in CSPD.
--
-- Purpose:
-- Tests mobility and equipment-type/location restrictions.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Washer Disinfector', 'WASHER_DISINFECTOR', 'STERILE_PROCESSING',
     'AT-5001', 'SN-5001', 'AVAILABLE', false, 5, 4);


-- ------------------------------------------------------------
-- 6. Patient Monitor
-- Initial Location: Pre-Op / Pre-Op Bay 1
-- Status: AVAILABLE
-- Mobile: true
--
-- Test Cases:
-- - Pre-Op -> ICU:
--   ALLOWED.
--
-- - Pre-Op -> Surgery:
--   ALLOWED.
--
-- Purpose:
-- Tests normal movement of patient monitoring equipment
-- between multiple clinical departments.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor', 'PATIENT_MONITOR', 'LOGISTICS',
     'AT-6001', 'SN-6001', 'AVAILABLE', true, 6, 5);


-- ------------------------------------------------------------
-- 7. C-Arm
-- Initial Location: Surgery / OR-1
-- Status: AVAILABLE
-- Mobile: true
--
-- Test Cases:
-- - Surgery -> Imaging:
--   ALLOWED.
--
-- - Surgery -> another Surgery room:
--   ALLOWED.
--
-- - Surgery -> CSPD:
--   REJECTED if C-Arm movement is restricted to approved
--   clinical/imaging departments.
--
-- Purpose:
-- Main test case for equipment-type / department validation.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('C-Arm', 'C_ARM', 'IMAGING',
     'AT-7001', 'SN-7001', 'AVAILABLE', true, 1, 1);


-- ------------------------------------------------------------
-- 8. Monitor - Maintenance
-- Initial Location: ICU / ICU-101
-- Status: UNDER_MAINTENANCE
-- Mobile: true
--
-- Test Case:
-- - Attempt to move to any other room or department:
--   REJECTED because equipment under maintenance cannot be moved.
--
-- Purpose:
-- Tests maintenance-status movement validation.
-- Note that mobile = true intentionally. This confirms that the
-- rejection is caused by status, not by the mobility rule.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Monitor - Maintenance', 'PATIENT_MONITOR', 'LOGISTICS',
     'AT-8001', 'SN-8001', 'UNDER_MAINTENANCE', true, 4, 3);

-- ============================================================
-- Equipment #9-#20 - Frontend / Lifecycle Demo Data
-- ============================================================
-- These records provide a realistic mix of equipment and statuses
-- for EquipmentList, filtering, status badges, dashboard counts,
-- equipment details, and maintenance history.
--
-- Unlike Equipment #1-#8, these records are mainly intended
-- for frontend/demo coverage rather than isolated move validation.
-- ============================================================


-- ------------------------------------------------------------
-- 9. Infusion Pump 02
-- Location: ICU / ICU-101
-- Status: IN_USE
-- Mobile: true
--
-- Purpose:
-- Frontend display of equipment currently being used.
-- Also provides another example of equipment that should not
-- be moved while IN_USE.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Infusion Pump 02', 'INFUSION_PUMP', 'LOGISTICS',
     'AT-4002', 'SN-4002', 'IN_USE', true, 4, 3);


-- ------------------------------------------------------------
-- 10. Infusion Pump 03
-- Location: Pre-Op / Pre-Op Bay 1
-- Status: DIRTY
-- Mobile: true
--
-- Purpose:
-- Represents equipment that has been used and is waiting
-- to enter the cleaning process.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Infusion Pump 03', 'INFUSION_PUMP', 'LOGISTICS',
     'AT-4003', 'SN-4003', 'DIRTY', true, 6, 5);


-- ------------------------------------------------------------
-- 11. Patient Monitor 02
-- Location: CSPD / Clean Storage
-- Status: IN_CLEANING
-- Mobile: true
--
-- Purpose:
-- Tests frontend display of the cleaning lifecycle.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor 02', 'PATIENT_MONITOR', 'LOGISTICS',
     'AT-6002', 'SN-6002', 'IN_CLEANING', true, 5, 4);


-- ------------------------------------------------------------
-- 12. Patient Monitor 03
-- Location: ICU / ICU-101
-- Status: AVAILABLE
-- Mobile: true
--
-- Purpose:
-- General available equipment for normal frontend display,
-- equipment details, movement, and maintenance history.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor 03', 'PATIENT_MONITOR', 'LOGISTICS',
     'AT-6003', 'SN-6003', 'AVAILABLE', true, 4, 3);


-- ------------------------------------------------------------
-- 13. Ultrasound 03
-- Location: Imaging / CT-1
-- Status: RESERVED
-- Mobile: true
--
-- Purpose:
-- Displays an extended equipment status that is supported
-- by EquipmentStatus but is not part of the current core
-- lifecycle state machine.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound 03', 'ULTRASOUND', 'IMAGING',
     'AT-2003', 'SN-2003', 'RESERVED', true, 3, 2);


-- ------------------------------------------------------------
-- 14. C-Arm 02
-- Location: Surgery / OR-2
-- Status: IN_USE
-- Mobile: true
--
-- Purpose:
-- Provides another realistic imaging device currently in use.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('C-Arm 02', 'C_ARM', 'IMAGING',
     'AT-7002', 'SN-7002', 'IN_USE', true, 2, 1);


-- ------------------------------------------------------------
-- 15. Anesthesia Machine 02
-- Location: Surgery / OR-2
-- Status: OUT_OF_SERVICE
-- Mobile: false
--
-- Purpose:
-- Displays equipment that is unavailable because it has been
-- removed from normal clinical service.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Anesthesia Machine 02', 'ANESTHESIA_MACHINE', 'PROCEDURE',
     'AT-1002', 'SN-1002', 'OUT_OF_SERVICE', false, 2, 1);


-- ------------------------------------------------------------
-- 16. Patient Monitor 04
-- Location: ICU / ICU-101
-- Status: UNDER_MAINTENANCE
-- Mobile: true
--
-- Purpose:
-- Used for MaintenanceList and equipment maintenance history.
-- An active maintenance record can be linked to this equipment.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor 04', 'PATIENT_MONITOR', 'LOGISTICS',
     'AT-6004', 'SN-6004', 'UNDER_MAINTENANCE', true, 4, 3);


-- ------------------------------------------------------------
-- 17. Infusion Pump 04
-- Location: Materials Management / Supply Room
-- Status: IN_TRANSIT
-- Mobile: true
--
-- Purpose:
-- Displays an extended logistics status.
-- IN_TRANSIT is supported by EquipmentStatus but is outside
-- the current core lifecycle state machine.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Infusion Pump 04', 'INFUSION_PUMP', 'LOGISTICS',
     'AT-4004', 'SN-4004', 'IN_TRANSIT', true, 7, 6);


-- ------------------------------------------------------------
-- 18. Patient Monitor 05
-- Location: CSPD / Clean Storage
-- Status: STERILE
-- Mobile: true
--
-- Purpose:
-- Represents equipment that has completed cleaning and
-- sterilization and is ready for the next lifecycle transition.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor 05', 'PATIENT_MONITOR', 'LOGISTICS',
     'AT-6005', 'SN-6005', 'STERILE', true, 5, 4);


-- ------------------------------------------------------------
-- 19. Ultrasound 04
-- Location: Imaging / CT-1
-- Status: LOST
-- Mobile: true
--
-- Purpose:
-- Displays an exception status for equipment that cannot
-- currently be located.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound 04', 'ULTRASOUND', 'IMAGING',
     'AT-2004', 'SN-2004', 'LOST', true, 3, 2);


-- ------------------------------------------------------------
-- 20. C-Arm 03
-- Location: Surgery / OR-1
-- Status: AVAILABLE
-- Mobile: true
--
-- Purpose:
-- General available imaging equipment.
-- Can be used for frontend display, equipment details,
-- normal movement, and maintenance history.
-- ------------------------------------------------------------
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('C-Arm 03', 'C_ARM', 'IMAGING',
     'AT-7003', 'SN-7003', 'AVAILABLE', true, 1, 1);

-- ============================================================
-- Maintenance Records - Frontend / Demo Data
-- ============================================================
-- Purpose:
-- 1. Cover all MaintenanceStatus values:
--    SCHEDULED, IN_PROGRESS, COMPLETED, CANCELED
-- 2. Cover all MaintenanceType values:
--    PREVENTIVE, CORRECTIVE, INSPECTION, CALIBRATION
-- 3. Provide equipment with:
--    - no maintenance history
--    - one maintenance record
--    - multiple maintenance records
--    - active maintenance
--    - completed maintenance history
-- ============================================================


-- ------------------------------------------------------------
-- Equipment #1 - Anesthesia Machine
-- Completed preventive maintenance
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        1,
        'PREVENTIVE',
        'COMPLETED',
        '2026-03-10 09:00:00',
        '2026-03-10 10:30:00',
        1,
        'Michael Wang',
        'Routine preventive maintenance',
        'Equipment passed inspection and returned to service.',
        '2026-03-01 08:00:00',
        '2026-03-10 10:30:00'
    );


-- ------------------------------------------------------------
-- Equipment #1 - Anesthesia Machine
-- Upcoming annual inspection
-- Gives Equipment #1 multiple maintenance records
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        1,
        'INSPECTION',
        'SCHEDULED',
        '2026-10-15 09:00:00',
        NULL,
        1,
        NULL,
        'Annual safety inspection',
        'Scheduled annual inspection.',
        '2026-09-15 08:00:00',
        '2026-09-15 08:00:00'
    );


-- ------------------------------------------------------------
-- Equipment #3 - Ultrasound - Available
-- Completed calibration
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        3,
        'CALIBRATION',
        'COMPLETED',
        '2026-05-12 13:00:00',
        '2026-05-12 14:15:00',
        3,
        'Michael Wang',
        'Routine ultrasound calibration',
        'Calibration completed within manufacturer tolerance.',
        '2026-05-05 09:00:00',
        '2026-05-12 14:15:00'
    );


-- ------------------------------------------------------------
-- Equipment #4 - Infusion Pump
-- Completed inspection
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        4,
        'INSPECTION',
        'COMPLETED',
        '2026-06-18 10:00:00',
        '2026-06-18 10:45:00',
        4,
        'Michael Wang',
        'Infusion pump safety inspection',
        'Battery, alarm, and flow checks passed.',
        '2026-06-10 11:00:00',
        '2026-06-18 10:45:00'
    );


-- ------------------------------------------------------------
-- Equipment #8 - Monitor - Maintenance
-- Active corrective maintenance
-- Equipment itself is UNDER_MAINTENANCE
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        8,
        'CORRECTIVE',
        'IN_PROGRESS',
        '2026-09-21 08:00:00',
        NULL,
        4,
        'Michael Wang',
        'Investigating intermittent monitor failure',
        'Diagnostic testing is currently in progress.',
        '2026-09-20 15:00:00',
        '2026-09-21 08:30:00'
    );


-- ------------------------------------------------------------
-- Equipment #8 - Monitor - Maintenance
-- Previous completed preventive maintenance
-- Gives active equipment some historical maintenance
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        8,
        'PREVENTIVE',
        'COMPLETED',
        '2026-02-20 09:30:00',
        '2026-02-20 10:20:00',
        4,
        'Michael Wang',
        'Routine preventive maintenance',
        'No issues found during preventive maintenance.',
        '2026-02-12 13:00:00',
        '2026-02-20 10:20:00'
    );


-- ------------------------------------------------------------
-- Equipment #12 - Patient Monitor 03
-- Canceled inspection
-- Covers CANCELED status
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        12,
        'INSPECTION',
        'CANCELED',
        '2026-08-15 11:00:00',
        NULL,
        4,
        NULL,
        'Scheduled electrical safety inspection',
        'Canceled and will be rescheduled.',
        '2026-08-05 10:00:00',
        '2026-08-14 16:00:00'
    );


-- ------------------------------------------------------------
-- Equipment #16 - Patient Monitor 04
-- Active corrective maintenance
-- Equipment itself is UNDER_MAINTENANCE
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        16,
        'CORRECTIVE',
        'IN_PROGRESS',
        '2026-09-20 08:00:00',
        NULL,
        4,
        'Michael Wang',
        'Repairing intermittent display failure',
        'Display module is being evaluated for replacement.',
        '2026-09-19 14:00:00',
        '2026-09-20 08:30:00'
    );


-- ------------------------------------------------------------
-- Equipment #16 - Patient Monitor 04
-- Previous completed calibration
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        16,
        'CALIBRATION',
        'COMPLETED',
        '2026-04-08 13:00:00',
        '2026-04-08 14:00:00',
        4,
        'Michael Wang',
        'Patient monitor calibration',
        'Calibration completed successfully.',
        '2026-04-01 09:00:00',
        '2026-04-08 14:00:00'
    );


-- ------------------------------------------------------------
-- Equipment #20 - C-Arm 03
-- Future preventive maintenance
-- ------------------------------------------------------------
INSERT INTO maintenance_records
(
    equipment_id,
    maintenance_type,
    status,
    scheduled_date,
    completed_date,
    requested_by_person_id,
    performed_by,
    description,
    notes,
    created_at,
    updated_at
)
VALUES
    (
        20,
        'PREVENTIVE',
        'SCHEDULED',
        '2026-11-05 08:30:00',
        NULL,
        3,
        NULL,
        'Scheduled preventive maintenance for C-Arm',
        'Preventive maintenance scheduled with biomedical engineering.',
        '2026-09-18 10:00:00',
        '2026-09-18 10:00:00'
    );