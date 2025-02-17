package com.example.demo.service;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired // Inyección de dependencia para acceder al repositorio
    ClientRepository clientRepository;

    // Obtener todos los clientes
    public ArrayList<Client> getAllClients(){
        return (ArrayList<Client>) clientRepository.findAll();
    }

    // Obtener un cliente por ID
    public Client getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.orElse(null);  // Retorna null si no se encuentra el cliente
    }

    // Guardar un nuevo cliente o actualizar uno existente
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // Eliminar un cliente
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
