package com.example.inventorycontrol.service;

import com.example.inventorycontrol.exception.DuplicateResourceException;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductService productService;

    @Autowired
    public ProviderService(ProviderRepository providerRepository, ProductService productService) {
        this.providerRepository = providerRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Provider> getProviderById(Long id) {
        return providerRepository.findById(id);
    }

    @Transactional
    public Provider createProvider(Provider provider) {
        if (providerRepository.findByEmail(provider.getEmail()).isPresent()) {
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
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar: proveedor con ID " + id + " no existe."));

        Set<Product> products = provider.getProducts();
        if (products != null && !products.isEmpty()) {
            Set<Product> productsCopy = new HashSet<>(products);
            for (Product product : productsCopy) {
                productService.deleteProduct(product.getId());
                products.remove(product);
            }
        }
        providerRepository.delete(provider);
    }
}