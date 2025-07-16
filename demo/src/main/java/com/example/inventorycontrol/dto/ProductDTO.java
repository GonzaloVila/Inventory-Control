package com.example.inventorycontrol.dto;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private int stock;
    private double price;
    private Integer categoryId;
    private Integer providerId;

    public ProductDTO(){}

    public ProductDTO(Long id, String name, String description, int stock, double price, Integer categoryId, Integer providerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.categoryId = categoryId;
        this.providerId = providerId;
    }
}
