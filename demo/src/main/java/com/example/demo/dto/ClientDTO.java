package com.example.demo.dto;

public class ClientDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String adress;

    public ClientDTO (){}

    public ClientDTO(Integer id, String name, String email, String password, String phone, String adress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.adress = adress;
    }
}
