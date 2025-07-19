package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByProvider(Provider provider);
}
