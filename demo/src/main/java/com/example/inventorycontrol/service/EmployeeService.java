package com.example.inventorycontrol.service;
import com.example.inventorycontrol.exception.DuplicateResourceException;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Employee;
import com.example.inventorycontrol.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    // Crear un empleado
    @Transactional
    public Employee createEmployee(Employee employee){
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un empleado con el email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    // Obtener todos los empleados
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    // Obtener un empleado por ID
    public Optional<Employee> getEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado con ID " + id + " no encontrado.");
        }
        return Optional.of(employeeRepository.getReferenceById(id));
    }

    // Actualizar un empleado
    @Transactional
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: empleado con ID " + employee.getId() + " no existe.");
        }
        return employeeRepository.save(employee);
    }

    // Eliminar un empleado
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: empleado con ID " + id + " no existe.");
        }
        employeeRepository.deleteById(id);
    }
}
