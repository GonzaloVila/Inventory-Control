package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.Provider;

public class ProviderDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;

    public ProviderDTO(){}

    public ProviderDTO(Provider provider) {
        this.id = provider.getId();
        this.name = provider.getName();
        this.address = provider.getAddress();
        this.phone = provider.getPhone();
        this.email = provider.getEmail();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Opcional: Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
}