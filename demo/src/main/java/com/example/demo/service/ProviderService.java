package com.example.demo.service;
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
        return providerRepository.save(provider);
    }

    // Obtener todos los proveedor
    public List<Provider> getAllProviders(){
        return providerRepository.findAll();
    }

    // Obtener un proveedor por ID
    public Optional<Provider> getProviderById(Long id) {
        return Optional.of(providerRepository.getReferenceById(id));
    }

    // Actualizar un proveedor
    public Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    // Eliminar un proveedor
    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }
}
