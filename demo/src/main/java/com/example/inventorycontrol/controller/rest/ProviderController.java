package com.example.inventorycontrol.controller.rest;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    @Autowired
    ProviderService providerService;

    // Obtener todos los proveedores
    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders(){
        List<Provider> providers = providerService.getAllProviders();
        return new ResponseEntity<>(providers, HttpStatus.OK);
    }

    // Obtener un proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id){
        Optional<Provider> providerOptional = providerService.getProviderById(id);
        return providerOptional.map(provider -> new ResponseEntity<>(provider, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado."));
    }

    // Crear un nuevo proveedor
    @PostMapping
    public ResponseEntity<Provider> createProvider(@RequestBody Provider provider) {
        Provider newProvider = providerService.createProvider(provider);
        return new ResponseEntity<>(newProvider, HttpStatus.CREATED);
    }

    // Actualizar un proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long id, @RequestBody Provider providerDetails) {
        Provider updatedProvider = providerService.updateProvider(id, providerDetails);
        return new ResponseEntity<>(updatedProvider, HttpStatus.OK);
    }

    // Eliminar un proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}