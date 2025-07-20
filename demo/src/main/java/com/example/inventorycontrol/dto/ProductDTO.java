package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.Product;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private int stock;
    private double price;
    private Long categoryId; // Cambiado a Long
    private Long providerId; // Cambiado a Long

    public ProductDTO(){}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.price = product.getPrice();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
        }
        if (product.getProvider() != null) {
            this.providerId = product.getProvider().getId();
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getStock() { return stock; }
    public double getPrice() { return price; }
    public Long getCategoryId() { return categoryId; }
    public Long getProviderId() { return providerId; }

    // Opcional: Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrice(double price) { this.price = price; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }
}