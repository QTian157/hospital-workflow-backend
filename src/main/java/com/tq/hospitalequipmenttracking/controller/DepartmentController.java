package com.tq.hospitalequipmenttracking.controller;

import com.tq.hospitalequipmenttracking.dto.response.DepartmentResponse;
import com.tq.hospitalequipmenttracking.exception.ApiResponse;
import com.tq.hospitalequipmenttracking.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','GUEST')")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {

        List<DepartmentResponse> departmentResponses = departmentService.getAllDepartments();
        return ResponseEntity.ok(
                ApiResponse.success(departmentResponses)
        );

    }
}
