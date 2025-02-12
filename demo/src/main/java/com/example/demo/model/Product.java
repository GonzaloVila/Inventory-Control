package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
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

    public Product(){}
}
