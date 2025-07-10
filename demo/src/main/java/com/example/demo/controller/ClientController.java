package com.example.demo.controller;

import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    //Obtener todos los clientes
    @GetMapping
    public List<Client> getAllClients(){
        return clientService.getAllClients();
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public Optional<Client> getClientById(@PathVariable Long id){
        return clientService.getClientById(id);
    }

    // Crear un nuevo cliente
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.updateClient(client);
    }

    // Actualizar un cliente existente
    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        return clientService.updateClient(client);
    }
}
