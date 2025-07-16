package com.example.inventorycontrol.service;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Category;
import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.CategoryRepository;
import com.example.inventorycontrol.repository.ProductRepository;
import com.example.inventorycontrol.repository.ProviderRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProviderRepository providerRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProviderRepository providerRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.providerRepository = providerRepository;
    }

    //Crear un producto
    @Transactional
    public Product createProduct(Product product) {
        if (product.getId() != null) {
            throw new IllegalArgumentException("El ID del producto debe ser nulo para una nueva creación.");
        }
        return updateProduct(product); //Reuso de validaciones
    }

    // Obtener todos los productos
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // Obtener un producto por ID
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        // Usa findById() para obtener un Optional real
        // Luego usa .orElseThrow() para lanzar la excepción si no se encuentra
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado."));
    }

    // Actualizar un producto
    public Product updateProduct(Product product) {
        if (product.getCategoryId() == null) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo.");
        }
        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + product.getCategoryId() + " no encontrada."));
        product.setCategory(category);

        if (product.getProviderId() == null) {
            throw new IllegalArgumentException("El ID del proveedor no puede ser nulo.");
        }
        Provider provider = providerRepository.findById(product.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID " + product.getProviderId() + " no encontrado."));
        product.setProvider(provider);

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
