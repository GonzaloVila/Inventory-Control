package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.model.Client;

public class ClientDTO {
    private Long id;
    private String name;
    private String lastName;
    private int document;
    private String email;
    private String phone;
    private String address;

    public ClientDTO (){}

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.lastName = client.getLastName();
        this.document = client.getDocument();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.address = client.getAddress();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLastName() {
        return lastName;
    }
    public int getDocument() {
        return document;
    }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    // Opcional: Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setDocument(int document) {
        this.document = document;
    }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
}