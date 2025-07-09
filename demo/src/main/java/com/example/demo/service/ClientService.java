package com.example.demo.service;
import com.example.demo.model.Category;
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
        return clientRepository.save(client);
    }

    // Obtener todos los clientes
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    // Obtener un cliente por ID
    public Optional<Client> getClientById(Long id) {
        return Optional.of(clientRepository.getReferenceById(id));
    }

    // Actualizar un cliente
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // Eliminar un cliente
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
