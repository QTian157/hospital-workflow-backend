package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.LoginRequest;
import com.tq.hospitalequipmenttracking.dto.request.RegisterRequest;
import com.tq.hospitalequipmenttracking.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
