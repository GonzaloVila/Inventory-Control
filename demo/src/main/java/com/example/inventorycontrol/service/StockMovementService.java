package com.example.inventorycontrol.service;

import com.example.inventorycontrol.repository.CustomerOrderRepository;
import com.example.inventorycontrol.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMovementService {
    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Object[]> getMonthlySalesData() {
        return customerOrderRepository.findMonthlySalesData();
    }
}