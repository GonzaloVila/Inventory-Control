package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Employee getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Employee employee_id) {
        this.employee_id = employee_id;
    }

    public Provider getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Provider provider_id) {
        this.provider_id = provider_id;
    }

    public Order(){}


}
