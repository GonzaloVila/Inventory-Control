package com.example.demo.service;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired // Inyección de dependencia para acceder al repositorio
    ClientRepository clientRepository;

    // Crear un cliente
    public Client createClient (Client client){
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un cliente con el email: " + client.getEmail());
        }
        return clientRepository.save(client);
    }

    // Obtener todos los clientes
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    // Obtener un cliente por ID
    public Optional<Client> getClientById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado.");
        }
        return Optional.of(clientRepository.getReferenceById(id));
    }

    // Actualizar un cliente
    public Client updateClient(Client client) {
        if (!clientRepository.existsById(client.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: cliente con ID " + client.getId() + " no existe.");
        }
        return clientRepository.save(client);
    }

    // Eliminar un cliente
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: cliente con ID " + id + " no existe.");
        }
        clientRepository.deleteById(id);
    }
}
