package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.CustomerOrder; // Asume que tienes esta entidad
import java.time.LocalDate; // Usa LocalDate si tu entidad CustomerOrder usa LocalDate
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Para mapear listas

public class CustomerOrderDTO {

    private Long id; // Cambiado a Long
    private LocalDate date; // Cambiado a LocalDate
    private String state;
    private Double total;
    private Long employeeId; // Mantenemos el ID aquí para simplicidad
    // private EmployeeDTO employee; // Si quisieras todos los datos del empleado, cambiarías esto
    private ClientDTO client; // Anidamos ClientDTO
    private ProviderDTO provider; // Anidamos ProviderDTO
    private List<OrderItemDTO> items; // Anidamos OrderItemDTO

    public CustomerOrderDTO(){}

    // Constructor que toma una entidad CustomerOrder y la mapea al DTO
    public CustomerOrderDTO(CustomerOrder order) {
        this.id = order.getId();
        this.date = order.getDate();
        this.state = order.getState();
        this.total = order.getTotal();
        this.employeeId = order.getEmployeeId(); // Mantenemos el ID como en tu entidad

        // Mapear el cliente
        if (order.getClient() != null) {
            this.client = new ClientDTO(order.getClient());
        }

        // Mapear el proveedor
        if (order.getProvider() != null) {
            this.provider = new ProviderDTO(order.getProvider());
        }

        // Mapear los ítems de la orden
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            this.items = order.getItems().stream()
                    .map(OrderItemDTO::new) // Mapea cada OrderItem a un OrderItemDTO
                    .collect(Collectors.toList());
        } else {
            this.items = new ArrayList<>(); // Asegúrate de inicializar si no hay ítems
        }

        // Si quisieras el EmployeeDTO completo, tendrías que cargarlo aquí:
        // if (order.getEmployee() != null) {
        //     this.employee = new EmployeeDTO(order.getEmployee());
        // }
    }

    // --- Getters ---
    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getState() { return state; }
    public Double getTotal() { return total; }
    public Long getEmployeeId() { return employeeId; }
    // public EmployeeDTO getEmployee() { return employee; } // Si lo incluyes
    public ClientDTO getClient() { return client; }
    public ProviderDTO getProvider() { return provider; }
    public List<OrderItemDTO> getItems() { return items; }

    // Opcional: Setters (más útiles si también los usas para Request DTOs)
    public void setId(Long id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setState(String state) { this.state = state; }
    public void setTotal(Double total) { this.total = total; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    // public void setEmployee(EmployeeDTO employee) { this.employee = employee; }
    public void setClient(ClientDTO client) { this.client = client; }
    public void setProvider(ProviderDTO provider) { this.provider = provider; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}