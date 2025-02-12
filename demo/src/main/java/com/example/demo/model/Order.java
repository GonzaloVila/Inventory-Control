package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String state;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee_id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider_id;

    public Order(){}

    public void setId(Long id) {
        this.id = id;
    }
}
