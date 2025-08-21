package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Consulta para obtener los productos más vendidos
    @Query("SELECT oi.product, SUM(oi.quantity) as totalSold " +
            "FROM OrderItem oi " +
            "GROUP BY oi.product " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
}