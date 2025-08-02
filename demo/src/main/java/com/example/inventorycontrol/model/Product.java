package com.example.inventorycontrol.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Nuevo campo para el soft delete
    *  Se utiliza para que se pueda borrar un proveedor y
    * poner como inactivo el producto cuando el proveedor no existe **/
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // Por defecto, un producto recién creado está activo

    private String name;
    private String description;
    private int stock;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference("product-category")
    @NotNull(message = "Debes seleccionar una categoría.")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    @JsonBackReference("product-provider")
    @NotNull(message = "Debes seleccionar un proveedor.")
    private Provider provider;

    public Product(){}

    public Product(Long id, Category category, Provider provider, String name, String description, int stock, double price) {
        this.id = id;
        this.category = category;
        this.provider = provider;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.isActive = true;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", category=" + category +
                ", provider=" + provider +
                '}';
    }
}
