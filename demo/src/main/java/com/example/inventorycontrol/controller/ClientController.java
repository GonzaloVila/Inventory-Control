package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.model.Client;
import com.example.inventorycontrol.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/client")
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
