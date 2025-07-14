package com.example.demo.controller;

import com.example.demo.model.ERol;
import com.example.demo.model.Rol;
import com.example.demo.model.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Crear nuevo usuario
        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRol();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null) {
            Rol userRol = RolRepository.findByName(ERol.ROL_EMPLOYEE) // Rol por defecto si no se especifica
                    .orElseThrow(() -> new RuntimeException("Error: Rol is not found."));
            roles.add(userRol);
        } else {
            strRoles.forEach(rol -> {
                switch (rol) {
                    case "admin":
                        Rol adminRol = RolRepository.findByName(ERol.ROL_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol is not found."));
                        roles.add(adminRol);
                        break;
                    case "manager":
                        Rol managerRol = RolRepository.findByName(ERol.ROL_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol is not found."));
                        roles.add(managerRol);
                        break;
                    case "employee": // Fallthrough para roles por defecto
                    default:
                        Rol employeeRol = RolRepository.findByName(ERol.ROL_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Rol is not found."));
                        roles.add(employeeRol);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
