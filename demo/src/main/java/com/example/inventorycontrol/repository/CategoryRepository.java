package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long >{
    boolean existsByName(String name);
}
