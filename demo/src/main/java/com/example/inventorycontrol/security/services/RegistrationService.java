package com.example.inventorycontrol.security.services;

import com.example.inventorycontrol.model.ERol;
import com.example.inventorycontrol.model.Rol;
import com.example.inventorycontrol.model.User;
import com.example.inventorycontrol.payload.request.SignupRequest;
import com.example.inventorycontrol.repository.RolRepository;
import com.example.inventorycontrol.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public User registerUser(SignupRequest signupRequest) {
        // 1. Verificar si el nombre de usuario ya existe
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Error: ¡El nombre de usuario '" + signupRequest.getUsername() + "' ya está en uso!");
        }

        // 2. Verificar si el email ya existe
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Error: ¡El email '" + signupRequest.getEmail() + "' ya está en uso!");
        }

        // 3. Crear nuevo objeto User con email
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(), // ¡Ahora pasamos el email!
                encoder.encode(signupRequest.getPassword()));

        // 4. Asignar roles
        Set<String> strRoles = signupRequest.getRol();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Asigna un rol por defecto si no se especifican roles
            Rol userRol = rolRepository.findByName(ERol.ROL_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol de usuario (ROL_USER) no encontrado."));
            roles.add(userRol);
        } else {
            strRoles.forEach(rol -> {
                switch (rol) {
                    case "admin":
                        Rol adminRol = rolRepository.findByName(ERol.ROL_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol de administrador (ROL_ADMIN) no encontrado."));
                        roles.add(adminRol);
                        break;
                    case "mod":
                        Rol managerRol = rolRepository.findByName(ERol.ROL_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol de manager (ROL_MANAGER) no encontrado."));
                        roles.add(managerRol);
                        break;
                    case "user":
                        Rol userRol = rolRepository.findByName(ERol.ROL_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol de usuario (ROL_USER) no encontrado."));
                        roles.add(userRol);
                        break;
                    default:
                        // Si se envía un rol desconocido, se asigna un rol por defecto
                        // o lanza una excepción para evitar roles no válidos.
                        Rol defaultUserRol = rolRepository.findByName(ERol.ROL_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol de usuario (ROLE_USER) no encontrado para rol desconocido."));
                        roles.add(defaultUserRol);
                }
            });
        }

        user.setRoles(roles);

        // 5. Guardar el nuevo usuario
        return userRepository.save(user);
    }
}
