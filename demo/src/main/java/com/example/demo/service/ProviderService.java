package com.example.demo.service;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    ProviderRepository providerRepository;

    // Crear un proveedor
    public Provider createProvider(Provider provider){
        if (providerRepository.findByEmail(provider.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un proveedor con el email: " + provider.getEmail());
        }
        return providerRepository.save(provider);
    }

    // Obtener todos los proveedor
    public List<Provider> getAllProviders(){
        return providerRepository.findAll();
    }

    // Obtener un proveedor por ID
    public Optional<Provider> getProviderById(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado.");
        }
        return Optional.of(providerRepository.getReferenceById(id));
    }

    // Actualizar un proveedor
    public Provider updateProvider(Provider provider) {
        if (!providerRepository.existsById(provider.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: proveedor con ID " + provider.getId() + " no existe.");
        }
        return providerRepository.save(provider);
    }

    // Eliminar un proveedor
    public void deleteProvider(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: proveedor con ID " + id + " no existe.");
        }
        providerRepository.deleteById(id);
    }
}
