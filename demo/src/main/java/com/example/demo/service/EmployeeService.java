package com.example.demo.service;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired // Inyección de dependencia para acceder al repositorio
    EmployeeRepository employeeRepository;

    // Crear un empleado
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
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: empleado con ID " + employee.getId() + " no existe.");
        }
        return employeeRepository.save(employee);
    }

    // Eliminar un empleado
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: empleado con ID " + id + " no existe.");
        }
        employeeRepository.deleteById(id);
    }
}
