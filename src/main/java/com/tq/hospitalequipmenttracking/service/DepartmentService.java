package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> getAllDepartments();
}
