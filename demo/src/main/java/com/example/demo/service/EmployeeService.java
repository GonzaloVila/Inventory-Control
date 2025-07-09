package com.example.demo.service;
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
        return employeeRepository.save(employee);
    }

    // Obtener todos los empleados
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    // Obtener un empleado por ID
    public Optional<Employee> getEmployeeById(Long id) {
        return Optional.of(employeeRepository.getReferenceById(id));
    }

    // Actualizar un empleado
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Eliminar un empleado
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
