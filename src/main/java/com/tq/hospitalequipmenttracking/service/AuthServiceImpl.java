package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.dto.request.LoginRequest;
import com.tq.hospitalequipmenttracking.dto.request.RegisterRequest;
import com.tq.hospitalequipmenttracking.dto.response.AuthResponse;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;
import com.tq.hospitalequipmenttracking.model.Person;
import com.tq.hospitalequipmenttracking.model.UserAccount;
import com.tq.hospitalequipmenttracking.repository.PersonRepository;
import com.tq.hospitalequipmenttracking.repository.UserRepository;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("Username already exists.");
        }

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(request.getUserRole());
        user.setEnabled(true);

        if (request.getPersonId() != null) {
            Person person = personRepository.findById(request.getPersonId())
                    .orElseThrow(() -> new BadRequestException("Person id not found."));

            user.setPerson(person);
        }
        userRepository.save(user);
//        return new AuthResponse("Register successful. JWT not generated yet.");
        return new AuthResponse(
                null,
                "Registration successful. Please login."
        ); // for reality, we register -> receive message -> login again -> therefore, no token return at register

    }

    @Override
    public AuthResponse login(LoginRequest request){
        UserAccount user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password."));
        if (!user.isEnabled()) {
            throw new BadRequestException("User is not enabled.");
        }

        // passwordEncoder.matches(raw, encoded)
        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatch) {
            throw new BadRequestException("Invalid username or password.");
        }
        String token = jwtService.generateToken(user);
        return new AuthResponse(
                token,
                "Login successful"
        );
//        return new AuthResponse("Login successful.JWT not generated yet");
    }
}
