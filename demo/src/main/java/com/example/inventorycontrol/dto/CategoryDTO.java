package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.Category;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;

    public CategoryDTO(){}

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Opcional: Setters para Request DTOs
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}