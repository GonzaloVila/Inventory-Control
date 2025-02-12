package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    // Obtener todas las órdenes (GET)
    @GetMapping
    public ArrayList<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Obtener una orden por ID (GET)
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Crear una nueva orden (POST)
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    // Actualizar una orden existente (PUT)
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);  // Asegura que se actualiza la orden con el ID correcto
        return orderService.saveOrder(order);
    }

    // Eliminar una orden (DELETE)
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
