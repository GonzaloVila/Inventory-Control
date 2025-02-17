package com.example.demo.controller;

import com.example.demo.model.Provider;
import com.example.demo.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    ProviderService providerService;

    //Obtener todos los proveedores
    @GetMapping
    public ArrayList<Provider> getAllProviders(){
        return providerService.getAllProviders();
    }

    // Obtener un proveedor por ID
    @GetMapping("/{id}")
    public Provider getProviderById(@PathVariable Long id){
        return providerService.getProviderById(id);
    }

    // Crear un nuevo proveedor
    @PostMapping
    public Provider createProvider(@RequestBody Provider provider) {
        return providerService.saveProvider(provider);
    }

    // Actualizar un proveedor existente
    @PutMapping("/{id}")
    public Provider updateProvider(@PathVariable Long id, @RequestBody Provider provider) {
        provider.setId(id);
        return providerService.saveProvider(provider);
    }
}
