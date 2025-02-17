package com.example.demo.controller;


import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //Obtener todos los empleados
    @GetMapping
    public ArrayList<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    // Obtener un empleado por ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    // Crear un nuevo empleado
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    // Actualizar un empleado existente
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return employeeService.saveEmployee(employee);
    }
}
