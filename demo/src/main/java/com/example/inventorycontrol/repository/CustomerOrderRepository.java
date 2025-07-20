package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByProvider(Provider provider);

    //  cargar todas las órdenes con sus ítems, productos, clientes y proveedores
    @Query("SELECT DISTINCT co FROM CustomerOrder co " +
            "LEFT JOIN FETCH co.items oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH co.client cl " +
            "LEFT JOIN FETCH co.provider pr")
    List<CustomerOrder> findAllWithDetails();

    // cargar una sola orden con detalles
    @Query("SELECT co FROM CustomerOrder co " +
            "LEFT JOIN FETCH co.items oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH co.client cl " +
            "LEFT JOIN FETCH co.provider pr " +
            "WHERE co.id = :id")
    Optional<CustomerOrder> findByIdWithDetails(Long id);
}
