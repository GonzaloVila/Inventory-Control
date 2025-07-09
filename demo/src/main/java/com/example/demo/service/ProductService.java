package com.example.demo.service;
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
        return productRepository.save(product);
    }

    // Obtener todos los productos
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Product> getProductById(Long id) {
        return Optional.of(productRepository.getReferenceById(id));
    }

    // Actualizar un producto
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Eliminar un producto
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
