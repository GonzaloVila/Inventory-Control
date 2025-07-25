package com.example.inventorycontrol.service;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.model.Employee;
import com.example.inventorycontrol.repository.CustomerOrderRepository;
import com.example.inventorycontrol.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderService {

    @Autowired
    CustomerOrderRepository orderRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    // Crear una orden
    @Transactional
    public CustomerOrder createCustomerOrder(CustomerOrder customerOrder){
        if (customerOrder == null || customerOrder.getClient() == null || customerOrder.getProvider() == null || customerOrder.getEmployee() == null) {
            throw new IllegalArgumentException("Faltan datos requeridos (cliente, proveedor o empleado) para crear la orden.");
        }
        if (customerOrder.getEmployee() != null && customerOrder.getEmployee().getId() != null) {
            boolean employeeExists = employeeRepository.existsById(customerOrder.getEmployee().getId());
            if (!employeeExists) {
                throw new ResourceNotFoundException("Empleado no encontrado con ID: " + customerOrder.getEmployee().getId());
            }
        } else {
            throw new IllegalArgumentException("Empleado es requerido para la orden.");
        }
        CustomerOrder savedOrder = orderRepository.save(customerOrder);
        return orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error al recargar la orden después de crear."));
    }

    // Obtener todas las órdenes
    public List<CustomerOrder> getAllOrders(){
        return orderRepository.findAllWithDetails();
    }

    // Obtener una orden por ID
    public Optional<CustomerOrder> getOrderById(Long id) {
        Optional<CustomerOrder> order = orderRepository.findByIdWithDetails(id);
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