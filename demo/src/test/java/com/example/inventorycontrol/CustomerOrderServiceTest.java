package com.example.inventorycontrol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Client;
import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.CustomerOrderRepository;

import com.example.inventorycontrol.service.CustomerOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerOrderServiceTest {

    @Mock
    private CustomerOrderRepository orderRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    private CustomerOrder customerOrder;

    @BeforeEach
    void setUp() {
        customerOrder = new CustomerOrder();
        customerOrder.setId(1L);
        customerOrder.setClient(new Client()); // Assume valid client
        customerOrder.setProvider(new Provider()); // Assume valid provider
        customerOrder.setDate(LocalDate.now());
    }

    @Test
    void whenCreateCustomerOrder_withValidData_thenOrderIsSaved() {
        // Arrange
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(customerOrder);

        // Act
        CustomerOrder savedOrder = customerOrderService.createCustomerOrder(customerOrder);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getId());
        verify(orderRepository).save(customerOrder);
    }

    @Test
    void whenCreateCustomerOrder_withNullOrder_thenThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            customerOrderService.createCustomerOrder(null);
        });
        verify(orderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    void whenCreateCustomerOrder_withNullClient_thenThrowIllegalArgumentException() {
        // Arrange
        customerOrder.setClient(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            customerOrderService.createCustomerOrder(customerOrder);
        });
        verify(orderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    void whenGetAllOrders_thenReturnsOrderList() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of(customerOrder));

        // Act
        List<CustomerOrder> orders = customerOrderService.getAllOrders();

        // Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository).findAll();
    }

    @Test
    void whenGetOrderById_withExistingId_thenReturnsOrder() {
        // Arrange
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.getReferenceById(1L)).thenReturn(customerOrder);

        // Act
        Optional<CustomerOrder> foundOrder = customerOrderService.getOrderById(1L);

        // Assert
        assertTrue(foundOrder.isPresent());
        assertEquals(1L, foundOrder.get().getId());
        verify(orderRepository).existsById(1L);
        verify(orderRepository).getReferenceById(1L);
    }

    @Test
    void whenGetOrderById_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            customerOrderService.getOrderById(99L);
        });
    }

    @Test
    void whenUpdateOrder_withExistingId_thenReturnsUpdatedOrder() {
        // Arrange
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(customerOrder);

        // Act
        CustomerOrder updatedOrder = customerOrderService.updateOrder(customerOrder);

        // Assert
        assertNotNull(updatedOrder);
        verify(orderRepository).existsById(1L);
        verify(orderRepository).save(customerOrder);
    }

    @Test
    void whenUpdateOrder_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            customerOrderService.updateOrder(customerOrder);
        });
    }

    @Test
    void whenDeleteOrder_withExistingId_thenDeletesOrder() {
        // Arrange
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            customerOrderService.deleteOrder(1L);
        });
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void whenDeleteOrder_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            customerOrderService.deleteOrder(99L);
        });
    }
}
