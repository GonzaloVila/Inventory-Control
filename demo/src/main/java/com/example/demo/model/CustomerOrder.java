package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "employee_id")
    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider_id;

    private LocalDate date;
    private String state;
    private Double total;

    public CustomerOrder(){}

    public CustomerOrder(Long id, Client client, Employee employee, Long employeeId, Provider provider_id, LocalDate date, String state, Double total) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.employeeId = employeeId;
        this.provider_id = provider_id;
        this.date = date;
        this.state = state;
        this.total = total;
        this.items = new ArrayList<OrderItem>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
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

    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Provider getProvider_id() {
        return provider_id;
    }
    public void setProvider_id(Provider provider_id) {
        this.provider_id = provider_id;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public List<OrderItem> getItems() {
        return items;
    }
    public void setItem(List<OrderItem> items) {
        this.items = items;
    }
    public void addItem(OrderItem orderItem){
        items.add(orderItem);
    }
}