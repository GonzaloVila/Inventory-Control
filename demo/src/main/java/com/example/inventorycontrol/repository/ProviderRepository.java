package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByEmail(String email);

    // Método para encontrar TODOS los proveedores que están activos
    List<Provider> findByIsActiveTrue();

    // Encontrar proveedores activos por ese atributo:
    Optional<Provider> findByEmailAndIsActiveTrue(String email);
}
