package com.example.inventorycontrol.controller.rest;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Client;
import com.example.inventorycontrol.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
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
    public ResponseEntity<Client> getClientById(@PathVariable Long id){
        Optional<Client> clientOptional = clientService.getClientById(id);
        return clientOptional.map(client -> new ResponseEntity<>(client, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado."));
    }

    // Crear un nuevo cliente
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        Client newClient = clientService.createClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED).getBody();
    }

    // Actualizar un cliente existente
    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        return clientService.updateClient(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
