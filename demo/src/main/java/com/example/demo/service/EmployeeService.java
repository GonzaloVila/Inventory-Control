package com.example.demo.service;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired // Inyección de dependencia para acceder al repositorio
    EmployeeRepository employeeRepository;

    // Obtener todos los empleados
    public ArrayList<Employee> getAllEmployees(){
        return (ArrayList<Employee>) employeeRepository.findAll();
    }

    // Obtener un empleado por ID
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);  // Retorna null si no se encuentra el empleado
    }

    // Guardar un nuevo empleado o actualizar uno existente
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Eliminar un empleado
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
