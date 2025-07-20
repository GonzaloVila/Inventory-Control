package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.CustomerOrder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Para mapear listas

public class CustomerOrderDTO {

    private Long id; // Cambiado a Long
    private LocalDate date; // Cambiado a LocalDate
    private String state;
    private Double total;
    private Long employeeId; // Mantenemos el ID aquí para simplicidad
    private ClientDTO client; // Anidamos ClientDTO
    private ProviderDTO provider; // Anidamos ProviderDTO
    private List<OrderItemDTO> items; // Anidamos OrderItemDTO

    public CustomerOrderDTO(){}

    // Constructor que toma una entidad CustomerOrder y la mapea al DTO
    public CustomerOrderDTO(CustomerOrder order) {
        this.id = order.getId();
        this.date = order.getDate();
        this.state = order.getState();

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
                    .map(OrderItemDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.items = new ArrayList<>();
        }
    }

    // --- Getters ---
    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getState() { return state; }
    public Double getTotal() { return total; }
    public ClientDTO getClient() { return client; }
    public ProviderDTO getProvider() { return provider; }
    public List<OrderItemDTO> getItems() { return items; }

    // Opcional: Setters  para Request DTOs
    public void setId(Long id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setState(String state) { this.state = state; }
    public void setTotal(Double total) { this.total = total; }
    public void setClient(ClientDTO client) { this.client = client; }
    public void setProvider(ProviderDTO provider) { this.provider = provider; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}