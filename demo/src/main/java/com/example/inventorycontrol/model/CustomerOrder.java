package com.example.inventorycontrol.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "employee_id")
    private Long employeeId;


    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private LocalDate date;
    private String state;
    private Double total;

    public CustomerOrder(){
        this.items = new ArrayList<>();
    }

    public CustomerOrder(Long id, Client client, Employee employee, Long employeeId, Provider provider, LocalDate date, String state, Double total) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.employeeId = employeeId;
        this.provider = provider;
        this.date = date;
        this.state = state;
        this.total = total;
        this.items = new ArrayList<>();
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void addItem(OrderItem orderItem){ items.add(orderItem); }
}