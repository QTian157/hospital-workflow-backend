package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
