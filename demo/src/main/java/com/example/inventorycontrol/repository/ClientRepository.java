package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    // metodo para buscar un cliente y cargar sus órdenes al mismo tiempo
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.orders WHERE c.id = :id")
    Optional<Client> findByIdWithOrders(@Param("id") Long id);
}
