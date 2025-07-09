package com.example.demo.service;

import com.example.demo.model.CustomerOrder;
import com.example.demo.repository.CustomerOrderRepository;
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
        return orderRepository.save(customerOrder);
    }

    // Obtener todas las órdenes
    public List<CustomerOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    // Obtener una orden por ID
    public Optional<CustomerOrder> getOrderById(Long id) {
        return Optional.of(orderRepository.getReferenceById(id));
    }

    // Guardar una nueva orden o actualizar una existente
    public CustomerOrder saveOrder(CustomerOrder order) {
        return orderRepository.save(order);
    }

    // Eliminar una orden
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
