package com.example.demo.controller;

import com.example.demo.model.Provider;
import com.example.demo.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    ProviderService providerService;

    //Obtener todos los proveedores
    @GetMapping
    public List<Provider> getAllProviders(){
        return providerService.getAllProviders();
    }

    // Obtener un proveedor por ID
    @GetMapping("/{id}")
    public Optional<Provider> getProviderById(@PathVariable Long id){
        return providerService.getProviderById(id);
    }

    // Crear un nuevo proveedor
    @PostMapping
    public Provider createProvider(@RequestBody Provider provider) {
        return providerService.updateProvider(provider);
    }

    // Actualizar un proveedor existente
    @PutMapping("/{id}")
    public Provider updateProvider(@PathVariable Long id, @RequestBody Provider provider) {
        provider.setId(id);
        return providerService.updateProvider(provider);
    }
}
