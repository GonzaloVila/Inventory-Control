package com.example.demo.service;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    // Crear un producto
    public Product createProduct(Product product){
        if (product == null || product.getName() == null || product.getProvider() == null) {
            throw new IllegalArgumentException("Faltan datos requeridos para crear el producto");
        }
        return productRepository.save(product);
    }

    // Obtener todos los productos
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Product> getProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado.");
        }
        return Optional.of(productRepository.getReferenceById(id));
    }

    // Actualizar un producto
    public Product updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: producto con ID " + product.getId() + " no existe.");
        }
        return productRepository.save(product);
    }

    // Eliminar un producto
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: producto con ID " + id + " no existe.");
        }
        productRepository.deleteById(id);
    }
}
