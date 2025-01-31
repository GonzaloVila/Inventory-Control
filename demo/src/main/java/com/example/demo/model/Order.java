package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    private String state;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private int employee_id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private int provider_id;

    public Order(){}
}
