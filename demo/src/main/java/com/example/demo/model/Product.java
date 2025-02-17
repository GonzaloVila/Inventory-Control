package com.example.demo.model;

import jakarta.persistence.*;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int stock;
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category_id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Category category_id) {
        this.category_id = category_id;
    }

    public Provider getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Provider provider_id) {
        this.provider_id = provider_id;
    }

    public Product(){}
}
