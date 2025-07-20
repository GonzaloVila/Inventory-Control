package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.OrderItem;

public class OrderItemDTO {
    private Long id;
    private ProductDTO product;
    private Integer quantity;
    private Double priceAtOrder;

    public OrderItemDTO(){}

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.priceAtOrder = orderItem.getPriceAtOrder();
        if (orderItem.getProduct() != null) {
            this.product = new ProductDTO(orderItem.getProduct());
        }
    }

    public Long getId() { return id; }
    public ProductDTO getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public Double getPriceAtOrder() { return priceAtOrder; }

    // Opcional: para Request DTOs
    public void setId(Long id) { this.id = id; }
    public void setProduct(ProductDTO product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setPriceAtOrder(Double priceAtOrder) { this.priceAtOrder = priceAtOrder; }
}