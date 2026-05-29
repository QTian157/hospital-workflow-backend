package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.LoginRequest;
import com.tq.hospitalequipmenttracking.dto.request.RegisterRequest;
import com.tq.hospitalequipmenttracking.dto.response.AuthResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.model.UserAccount;
import com.tq.hospitalequipmenttracking.model.UserRole;
import com.tq.hospitalequipmenttracking.repository.PersonRepository;
import com.tq.hospitalequipmenttracking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;
    // this is the one to be tested -> real
    //    new AuthServiceImpl(
    //            fakeUserRepository,
    //            fakePersonRepository,
    //            fakePasswordEncoder,
    //            fakeJwtService
    //            );

    @Test
    void register_success_withoutPerson() {
        // 1. Create a fake request as if it came from the frontend
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setUserRole(UserRole.ADMIN);

        // 2. Mock the dependencies
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        // 3. Call the real register method
        AuthResponse response = authService.register(request);
        // 4. Check the response
        assertNull(response.getToken());
        assertEquals("Registration successful. Please login.", response.getMessage());
        // 5. Capture the user object passed into userRepository.save()
        ArgumentCaptor<UserAccount> userCaptor = ArgumentCaptor.forClass(UserAccount.class);
        verify(userRepository).save(userCaptor.capture());

        UserAccount savedUser = userCaptor.getValue();
        // 6. Check the saved user fields
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(UserRole.ADMIN, savedUser.getUserRole());
        assertTrue(savedUser.isEnabled());
        assertNull(savedUser.getPerson());
    }

    @Test
    void register_duplicateUsername_shouldThrowException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setUserRole(UserRole.ADMIN);

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.register(request)
        );

        assertEquals("Username already exists.", exception.getMessage());
        verify(userRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void login_success_shouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        UserAccount user = new UserAccount();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setUserRole(UserRole.ADMIN);
        user.setEnabled(true);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void login_wrongPassword_shouldThrowException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongPassword");

        UserAccount user = new UserAccount();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setUserRole(UserRole.ADMIN);
        user.setEnabled(true);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid username or password.", exception.getMessage());
        verify(jwtService, never()).generateToken(any(UserAccount.class));
    }
}