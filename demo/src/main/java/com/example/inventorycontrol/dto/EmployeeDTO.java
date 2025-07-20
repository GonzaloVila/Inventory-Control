package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.Employee;

public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private String rol;

    public EmployeeDTO(){}

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.rol = employee.getRol();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }

    // Opcional: Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRol(String rol) { this.rol = rol; }
}