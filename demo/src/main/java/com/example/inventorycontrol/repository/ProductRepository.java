package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.hibernate.annotations.Where;

import java.util.Optional;

@Repository

// "WHERE isActive = true" a todas las consultas generadas por JPA
@Where(clause = "is_active = true")
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Método para buscar productos por nombre (también respetará isActive = true por el @Where)
    Optional<Product> findByName(String name);

    // Método para "eliminar" un producto lógicamente
    @Modifying // Indica que esta consulta va a modificar la base de datos
    @Query("UPDATE Product p SET p.isActive = false WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);
}