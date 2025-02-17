package com.example.demo.service;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    // Obtener todos los productos
    public ArrayList<Product> getAllProducts(){
        return (ArrayList<Product>) productRepository.findAll();
    }

    // Obtener un producto por ID
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);  // Retorna null si no se encuentra el producto
    }

    // Guardar un nuevo producto o actualizar uno existente
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Eliminar un producto
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
