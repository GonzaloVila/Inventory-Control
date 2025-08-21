package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Optional;

@Repository

// "WHERE isActive = true" a todas las consultas generadas por JPA
@Where(clause = "is_active = true")
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Nueva consulta para obtener todos los productos con sus categorías y proveedores
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.provider")
    List<Product> findAllWithCategoryAndProvider();

    // Metodo para buscar productos por nombre (también respetará isActive = true por el @Where)
    Optional<Product> findByName(String name);

    List<Product> findByProvider(Provider provider);

    // Metodo para "eliminar" un producto lógicamente
    @Modifying // Indica que esta consulta va a modificar la base de datos
    @Query("UPDATE Product p SET p.isActive = false WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);

    // Metodo para encontrar productos con un stock menor a un valor dado
    List<Product> findByStockLessThan(int stock);

    // Metodo para calcular el valor total del inventario
    @Query("SELECT SUM(p.price * p.stock) FROM Product p WHERE p.isActive = true")
    Double calculateTotalInventoryValue();
}