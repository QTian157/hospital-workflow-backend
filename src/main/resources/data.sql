-- Department
INSERT INTO departments (id, name) VALUES (1, 'Surgery');
INSERT INTO departments (id, name) VALUES (2, 'Imaging');
INSERT INTO departments (id, name) VALUES (3, 'ICU');
INSERT INTO departments (id, name) VALUES (4, 'CSPD');

-- Room
INSERT INTO rooms (id, name, department_id) VALUES (1, 'OR-1', 1);
INSERT INTO rooms (id, name, department_id) VALUES (2, 'OR-2', 1);
INSERT INTO rooms (id, name, department_id) VALUES (3, 'CT-1', 2);
INSERT INTO rooms (id, name, department_id) VALUES (4, 'ICU-101', 3);
INSERT INTO rooms (id, name, department_id) VALUES (5, 'Clean Storage', 4);