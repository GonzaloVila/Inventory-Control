package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    //Obtener todos los productos
    @GetMapping
    public ArrayList<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    // Crear un nuevo producto
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.saveProduct(product);
    }
}
