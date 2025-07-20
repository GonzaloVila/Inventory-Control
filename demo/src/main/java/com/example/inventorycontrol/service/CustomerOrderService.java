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

    @Autowired
    CustomerOrderRepository orderRepository;

    // Crear una orden
    public CustomerOrder createCustomerOrder(CustomerOrder customerOrder){
        if (customerOrder == null || customerOrder.getClient() == null || customerOrder.getProvider() == null) {
            throw new IllegalArgumentException("Faltan datos requeridos para crear la orden");
        }
        CustomerOrder savedOrder = orderRepository.save(customerOrder);
        // Asegúrate de que las relaciones (items, client, provider) estén cargadas para el DTO
        // Usamos findByIdWithDetails para recargar y asegurar la carga.
        return orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error al recargar la orden después de crear."));
    }

    // Obtener todas las órdenes
    public List<CustomerOrder> getAllOrders(){
        return orderRepository.findAllWithDetails(); // <--- ¡CAMBIO AQUÍ!
    }

    // Obtener una orden por ID
    public Optional<CustomerOrder> getOrderById(Long id) {
        // No necesitas existsById si usas el método findByIdWithDetails que ya devuelve Optional
        Optional<CustomerOrder> order = orderRepository.findByIdWithDetails(id); // <--- ¡CAMBIO AQUÍ!
        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Orden con ID " + id + " no encontrada.");
        }
        return order;
    }

    // Actualiza una orden
    public CustomerOrder updateOrder(CustomerOrder order) {
        if (!orderRepository.existsById(order.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: orden con ID " + order.getId() + " no existe.");
        }
        CustomerOrder updatedOrder = orderRepository.save(order);
        // Recarga para asegurar que las relaciones estén cargadas para el DTO
        return orderRepository.findByIdWithDetails(updatedOrder.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error al recargar la orden después de actualizar."));
    }

    // Eliminar una orden
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: orden con ID " + id + " no existe.");
        }
        orderRepository.deleteById(id);
    }
}