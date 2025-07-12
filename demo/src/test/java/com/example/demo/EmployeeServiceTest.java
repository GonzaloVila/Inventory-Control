package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("Jane Smith");
        employee.setEmail("jane.smith@example.com");
        employee.setRol("Developer");
    }

    @Test
    void whenCreateEmployee_withNonExistingEmail_thenEmployeeIsSaved() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee savedEmployee = employeeService.createEmployee(employee);

        // Assert
        assertNotNull(savedEmployee);
        assertEquals("jane.smith@example.com", savedEmployee.getEmail());
        verify(employeeRepository).save(employee);
    }

    @Test
    void whenCreateEmployee_withExistingEmail_thenThrowDuplicateResourceException() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            employeeService.createEmployee(employee);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void whenGetAllEmployees_thenReturnsEmployeeList() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertNotNull(employees);
        assertEquals(1, employees.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void whenGetEmployeeById_withExistingId_thenReturnsEmployee() {
        // Arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.getReferenceById(1L)).thenReturn(employee);

        // Act
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        // Assert
        assertTrue(foundEmployee.isPresent());
        assertEquals(1L, foundEmployee.get().getId());
    }

    @Test
    void whenGetEmployeeById_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(99L);
        });
    }

    @Test
    void whenUpdateEmployee_withExistingId_thenReturnsUpdatedEmployee() {
        // Arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // Assert
        assertNotNull(updatedEmployee);
        verify(employeeRepository).save(employee);
    }

    @Test
    void whenUpdateEmployee_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(employee);
        });
    }

    @Test
    void whenDeleteEmployee_withExistingId_thenDeletesEmployee() {
        // Arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            employeeService.deleteEmployee(1L);
        });
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void whenDeleteEmployee_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(99L);
        });
    }
}
