package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/customerOrder")
public class CustomerOrderController {

    @Autowired
    CustomerOrderService orderService;

    // Obtener todas las órdenes
    @GetMapping
    public List<CustomerOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    public Optional<CustomerOrder> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Crear una nueva orden
    @PostMapping
    public CustomerOrder createCustomerOrder(@RequestBody CustomerOrder order) {
        return orderService.createCustomerOrder(order);
    }

    // Actualizar una orden existente
    @PutMapping("/{id}")
    public CustomerOrder updateOrder(@PathVariable Long id, @RequestBody CustomerOrder order) {
        order.setId(id);
        return orderService.updateOrder(order);
    }

    // Eliminar una orden
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
