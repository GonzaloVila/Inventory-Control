package com.example.inventorycontrol;

import com.example.inventorycontrol.model.ERol;
import com.example.inventorycontrol.model.Rol;
import com.example.inventorycontrol.model.User;
import com.example.inventorycontrol.repository.RolRepository;
import com.example.inventorycontrol.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //  1. Asegurar que todos los roles existan
        Rol adminRol = rolRepository.findByName(ERol.ROL_ADMIN)
                .orElseGet(() -> rolRepository.save(new Rol(ERol.ROL_ADMIN)));

        Rol managerRol = rolRepository.findByName(ERol.ROL_MANAGER)
                .orElseGet(() -> rolRepository.save(new Rol(ERol.ROL_MANAGER)));

        Rol employeeRol = rolRepository.findByName(ERol.ROL_EMPLOYEE)
                .orElseGet(() -> rolRepository.save(new Rol(ERol.ROL_EMPLOYEE)));

        Rol userRol = rolRepository.findByName(ERol.ROL_USER)
                .orElseGet(() -> rolRepository.save(new Rol(ERol.ROL_USER)));

        System.out.println("Roles inicializados si no existían.");

        //  2. Asegurar que el usuario 'admin' exista y tenga el rol correcto
        Optional<User> adminUserOptional = userRepository.findByUsername("admin");
        if (adminUserOptional.isEmpty()) {
            System.out.println("Creando usuario ADMIN...");
            User adminUser = new User("admin", "admin@inventory.com", passwordEncoder.encode("admin123"));
            Set<Rol> roles = new HashSet<>();
            roles.add(adminRol);
            adminUser.setRoles(roles);
            userRepository.save(adminUser);
            System.out.println("Usuario ADMIN creado exitosamente.");
        } else {
            System.out.println("El usuario ADMIN ya existe. Verificando roles...");
            User adminUser = adminUserOptional.get();
            boolean hasAdminRole = adminUser.getRoles().stream().anyMatch(rol -> rol.getName().equals(ERol.ROL_ADMIN));
            if (!hasAdminRole) {
                System.out.println("El usuario ADMIN no tenía el rol ROL_ADMIN. Asignándolo...");
                adminUser.getRoles().add(adminRol);
                userRepository.save(adminUser);
                System.out.println("Rol ROL_ADMIN asignado al usuario ADMIN.");
            } else {
                System.out.println("El usuario ADMIN ya tiene el rol ROL_ADMIN. Todo en orden.");
            }
        }

        //  3. Asegurar que el usuario 'admin1' exista y tenga el rol correcto
        Optional<User> admin1UserOptional = userRepository.findByUsername("admin1");
        if (admin1UserOptional.isEmpty()) {
            System.out.println("Creando usuario ADMIN1...");
            User admin1User = new User("admin1", "admin1@inventory.com", passwordEncoder.encode("admin1234"));
            Set<Rol> roles = new HashSet<>();
            roles.add(adminRol);
            admin1User.setRoles(roles);
            userRepository.save(admin1User);
            System.out.println("Usuario ADMIN1 creado exitosamente.");
        } else {
            System.out.println("El usuario ADMIN1 ya existe. No se creará de nuevo.");
        }
    }
}