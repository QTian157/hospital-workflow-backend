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
-- Equipment
-- =========================
INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Anesthesia Machine', 'ANESTHESIA_MACHINE', 'PROCEDURE', 'AT-1001', 'SN-1001', 'AVAILABLE', false, 1, 1);

INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound - In Use', 'ULTRASOUND', 'IMAGING', 'AT-2001', 'SN-2001', 'IN_USE', true, 3, 2);

INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Ultrasound - Available', 'ULTRASOUND', 'IMAGING', 'AT-2002', 'SN-2002', 'AVAILABLE', true, 3, 2);

INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Infusion Pump', 'INFUSION_PUMP', 'MOBILE', 'AT-4001', 'SN-4001', 'AVAILABLE', true, 4, 3);

INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Sterilizer', 'WASHER_DISINFECTOR', 'STERILE_PROCESSING', 'AT-5001', 'SN-5001', 'AVAILABLE', false, 5, 4);

INSERT INTO equipments
(name, type, category, asset_tag, serial_number, status, mobile, current_room_id, department_id)
VALUES
    ('Patient Monitor', 'PATIENT_MONITOR', 'MOBILE', 'AT-6001', 'SN-6001', 'AVAILABLE', true, 6, 5);