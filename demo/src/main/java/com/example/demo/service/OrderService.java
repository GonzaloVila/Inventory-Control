package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired // Inyección de dependencia para acceder al repositorio
    OrderRepository orderRepository;


    // Obtener todas las órdenes
    public ArrayList<Order> getAllOrders(){
        return (ArrayList<Order>) orderRepository.findAll();
    }

    // Obtener una orden por ID
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);  // Retorna null si no se encuentra la orden
    }

    // Guardar una nueva orden o actualizar una existente
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Eliminar una orden
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
