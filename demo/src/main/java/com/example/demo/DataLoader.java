package com.example.demo;

import com.example.demo.model.ERol;
import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {
    // 1. Inyecta el RoleRepository usando @Autowired
    // Spring se encargará de crear una instancia de RolRepository y asignarla aquí
    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        // 2. Ahora puedes llamar a findByName() porque rolRepository es una instancia válida
        if (rolRepository.findByName(ERol.ROL_ADMIN).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_ADMIN));
        }
        if (rolRepository.findByName(ERol.ROL_MANAGER).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_MANAGER));
        }
        if (rolRepository.findByName(ERol.ROL_EMPLOYEE).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_EMPLOYEE));
        }
        if (rolRepository.findByName(ERol.ROL_VIEWER).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_VIEWER));
        }
        // Opcional: imprimir algo para saber que se ejecutó
        System.out.println("Roles inicializados si no existían.");
    }
}
