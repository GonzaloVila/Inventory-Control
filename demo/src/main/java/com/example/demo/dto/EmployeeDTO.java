package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeDTO {

    private Integer id;
    private String name;
    private String email;
    private String rol;
    private String password;

    public EmployeeDTO(){}

    public EmployeeDTO(Integer id, String name, String email, String rol, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rol = rol;
        this.password = password;
    }
}
