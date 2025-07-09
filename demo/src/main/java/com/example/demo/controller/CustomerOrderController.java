package com.example.demo.controller;

import com.example.demo.model.CustomerOrder;
import com.example.demo.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {

    @Autowired
    CustomerOrderService orderService;

    // Obtener todas las órdenes (GET)
    @GetMapping
    public ArrayList<CustomerOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Obtener una orden por ID (GET)
    @GetMapping("/{id}")
    public CustomerOrder getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Crear una nueva orden (POST)
    @PostMapping
    public CustomerOrder createOrder(@RequestBody CustomerOrder order) {
        return orderService.saveOrder(order);
    }

    // Actualizar una orden existente (PUT)
    @PutMapping("/{id}")
    public CustomerOrder updateOrder(@PathVariable Long id, @RequestBody CustomerOrder order) {
        order.setId(id);
        return orderService.saveOrder(order);
    }

    // Eliminar una orden (DELETE)
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
