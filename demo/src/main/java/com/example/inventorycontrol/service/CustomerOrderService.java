package com.example.inventorycontrol.service;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderService {

    @Autowired // Inyección de dependencia para acceder al repositorio
    CustomerOrderRepository orderRepository;

    // Crear una orden
    public CustomerOrder createCustomerOrder(CustomerOrder customerOrder){
        if (customerOrder == null || customerOrder.getClient() == null || customerOrder.getProvider() == null) {
            throw new IllegalArgumentException("Faltan datos requeridos para crear la orden");
        }
        return orderRepository.save(customerOrder);
    }

    // Obtener todas las órdenes
    public List<CustomerOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    // Obtener una orden por ID
    public Optional<CustomerOrder> getOrderById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Orden con ID " + id + " no encontrada.");
        }
        return Optional.of(orderRepository.getReferenceById(id));
    }

    // Actualiza una orden
    public CustomerOrder updateOrder(CustomerOrder order) {
        if (!orderRepository.existsById(order.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: orden con ID " + order.getId() + " no existe.");
        }
        return orderRepository.save(order);
    }

    // Eliminar una orden
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: orden con ID " + id + " no existe.");
        }
        orderRepository.deleteById(id);
    }
}
