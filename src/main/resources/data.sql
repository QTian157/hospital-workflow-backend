
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

-- Equipment
-- =========================
-- 1. anesthesia（正常 + non-mobile）: non-mobile move/ move 到 ICU（type 不允许）
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Anesthesia Machine', 'ANESTHESIA_MACHINE', 'PROCEDURE', 'AT-1001', 'SN-1001', 'AVAILABLE', false, 1, 1);

-- 2. ultrasound（IN_USE）: status 不允许 move
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound - In Use', 'ULTRASOUND', 'IMAGING', 'AT-2001', 'SN-2001', 'IN_USE', true, 2, 1);

-- 3. ultrasound（AVAILABLE): 正常跨 department move（Radiology → Surgery）
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound - Available', 'ULTRASOUND', 'IMAGING', 'AT-2002', 'SN-2002', 'AVAILABLE', true, 3, 2);

-- 4. infusion pump（mobile）: ICU → Pre-op / Surgery; mobile move
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Infusion Pump', 'INFUSION_PUMP', 'LOGISTICS', 'AT-4001', 'SN-4001', 'AVAILABLE', true, 4, 3);

-- 5. washer（CSPD only）:move 到 ICU（type 不允许）/non-mobile move
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Washer Disinfector', 'WASHER_DISINFECTOR', 'STERILE_PROCESSING', 'AT-5001', 'SN-5001', 'AVAILABLE', false, 5, 4);

-- 6. patient monitor（多场景）:PRE_OP → ICU/PRE_OP → Surgery
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor', 'PATIENT_MONITOR', 'LOGISTICS', 'AT-6001', 'SN-6001', 'AVAILABLE', true, 6, 5);

-- 7. C-arm（重点测试）:Surgery → Radiology/Surgery → CSPD
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('C-Arm', 'C_ARM', 'IMAGING', 'AT-7001', 'SN-7001', 'AVAILABLE', true, 1, 1);

-- 8. Under maintenance（关键）:status 不允许 move
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Monitor - Maintenance', 'PATIENT_MONITOR', 'LOGISTICS', 'AT-8001', 'SN-8001', 'UNDER_MAINTENANCE', true, 4, 3);