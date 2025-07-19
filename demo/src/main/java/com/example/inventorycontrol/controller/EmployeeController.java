package com.example.inventorycontrol.controller;


import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Employee;
import com.example.inventorycontrol.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //Obtener todos los empleados
    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    // Obtener un empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
        return employeeOptional.map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado."));
    }

    // Crear un nuevo empleado
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED); // 201 Created
    }

    // Actualizar un empleado existente
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return employeeService.updateEmployee(employee);
    }

    // Eliminar un empleado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}
