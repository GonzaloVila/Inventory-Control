package com.example.inventorycontrol.service;

import com.example.inventorycontrol.exception.DuplicateResourceException;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.ProductRepository;
import com.example.inventorycontrol.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProviderService(ProviderRepository providerRepository, ProductService productService, ProductRepository productRepository) {
        this.providerRepository = providerRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Provider> getAllProviders() {
        return providerRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Provider> getProviderById(Long id) {
        return providerRepository.findById(id);
    }

    @Transactional
    public Provider createProvider(Provider provider) {
        provider.setActive(true);
        if (providerRepository.findByEmailAndIsActiveTrue(provider.getEmail()).isPresent()) {
            throw new DuplicateResourceException("El proveedor con email " + provider.getEmail() + " ya existe.");
        }
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider updateProvider(Long id, Provider providerDetails) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado."));

        if (providerDetails.getEmail() != null && !providerDetails.getEmail().equals(provider.getEmail())) {
            if (providerRepository.findByEmail(providerDetails.getEmail()).isPresent()) {
                throw new DuplicateResourceException("El email " + providerDetails.getEmail() + " ya está en uso por otro proveedor.");
            }
        }

        provider.setName(providerDetails.getName());
        provider.setAddress(providerDetails.getAddress());
        provider.setPhone(providerDetails.getPhone());
        provider.setEmail(providerDetails.getEmail());

        return providerRepository.save(provider);
    }

    @Transactional
    public void deleteProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));
        List<Product> products = productRepository.findByProvider(provider);
        for (Product product : products) {
            product.setProvider(null);
            productRepository.save(product);
        }
        // En lugar de borrar, marcar como inactivo
        provider.setActive(false);
        providerRepository.save(provider);
    }
}