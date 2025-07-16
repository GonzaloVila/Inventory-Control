package com.example.inventorycontrol.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum como String
    @Column(length = 20, unique = true)
    private ERol name; // Usaremos un enum para los nombres de roles

    @ManyToMany(mappedBy = "roles") // Mapeado por la relación en User
    private Set<User> users = new HashSet<>();

    // Constructores, Getters y Setters
    public Rol() {}

    public Rol(ERol name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ERol getName() { return name; }
    public void setName(ERol name) { this.name = name; }
    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}
