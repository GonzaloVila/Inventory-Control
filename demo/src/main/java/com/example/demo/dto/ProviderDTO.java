package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProviderDTO {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String email;

    public ProviderDTO(){}

    public ProviderDTO(Integer id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
