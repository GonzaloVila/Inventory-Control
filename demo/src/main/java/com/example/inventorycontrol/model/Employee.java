package com.example.inventorycontrol.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "employee")
    private List<CustomerOrder> processedOrders;

    private String name;
    private String email;
    private String rol;
    private String password;

    public Employee(){}

    public Employee(Long id, String name, String email, String rol, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rol = rol;
        this.password = password;
        this.processedOrders = new ArrayList<CustomerOrder>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CustomerOrder> getProcessedOrders() {
        return processedOrders;
    }

    public void setProcessedOrders(List<CustomerOrder> processedOrders) {
        this.processedOrders = processedOrders;
    }

    public void addOrder(CustomerOrder order){
        processedOrders.add(order);
    }
}
