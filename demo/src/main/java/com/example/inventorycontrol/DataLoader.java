package com.example.inventorycontrol;

import com.example.inventorycontrol.model.ERol;
import com.example.inventorycontrol.model.Rol;
import com.example.inventorycontrol.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.findByName(ERol.ROL_ADMIN).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_ADMIN));
        }
        if (rolRepository.findByName(ERol.ROL_MANAGER).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_MANAGER));
        }
        if (rolRepository.findByName(ERol.ROL_EMPLOYEE).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_EMPLOYEE));
        }
        if (rolRepository.findByName(ERol.ROL_USER).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROL_USER));
        }

        // Opcional: imprimir para saber que se ejecutó
        System.out.println("Roles inicializados si no existían.");
    }
}
