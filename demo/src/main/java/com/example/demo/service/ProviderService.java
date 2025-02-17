package com.example.demo.service;
import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    ProviderRepository providerRepository;

    // Obtener todos los proveedor
    public ArrayList<Provider> getAllProviders(){
        return (ArrayList<Provider>) providerRepository.findAll();
    }

    // Obtener un proveedor por ID
    public Provider getProviderById(Long id) {
        Optional<Provider> provider = providerRepository.findById(id);
        return provider.orElse(null);  // Retorna null si no se encuentra el proveedor
    }

    // Guardar un nuevo proveedor o actualizar uno existente
    public Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    // Eliminar un proveedor
    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }
}
