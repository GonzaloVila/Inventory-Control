package com.example.demo.dto;

public class CategoryDTO {
    private Integer id;
    private String nombre;
    private String description;

    public CategoryDTO(){}

    public CategoryDTO(Integer id, String nombre, String description) {
        this.id = id;
        this.nombre = nombre;
        this.description = description;
    }

}
