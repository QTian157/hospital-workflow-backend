package com.tq.hospitalequipmenttracking.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tq.hospitalequipmenttracking.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {
    @NotBlank(message = "username cannot be null")
    private String username;
    @NotBlank(message = "password cannot be null")
    private String password;
    @NotNull(message = "userRole cannot be null")
    private UserRole userRole;
    private Long personId;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
