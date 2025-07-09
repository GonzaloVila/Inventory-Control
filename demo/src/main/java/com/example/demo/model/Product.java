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
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "provider_id", insertable = false, updatable = false)
    private Provider provider;

    @Column(name = "provider_id")
    private Long providerId;

    public Product(){}

    public Product(Long id, Category category, Provider provider, String name, String description, int stock, double price, Long categoryId, Long providerId) {
        this.id = id;
        this.category = category;
        this.provider = provider;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.categoryId = categoryId;
        this.providerId = providerId;
    }

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

    public Long getCategory_id() {
        return categoryId;
    }

    public void setCategory_id(Long category_id) {
        this.categoryId = categoryId;
    }

    public Long getProvider_id() {
        return providerId;
    }

    public void setProvider_id(Long  provider_id) {
        this.providerId = providerId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

}
