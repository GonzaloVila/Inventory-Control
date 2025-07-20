package com.example.inventorycontrol.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price_at_order", nullable = false)
    private Double priceAtOrder;

    public OrderItem(){}

    public OrderItem(Long id, CustomerOrder order, Product product, int quantity, Double priceAtOrder) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CustomerOrder getOrder() { return order; }
    public void setOrder(CustomerOrder order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Double getPriceAtOrder() { return priceAtOrder; }
    public void setPriceAtOrder(Double priceAtOrder) { this.priceAtOrder = priceAtOrder; }
}