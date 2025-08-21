package com.example.inventorycontrol.service;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Category;
import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.CategoryRepository;
import com.example.inventorycontrol.repository.OrderItemRepository;
import com.example.inventorycontrol.repository.ProductRepository;
import com.example.inventorycontrol.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProviderRepository providerRepository, CategoryRepository categoryRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.providerRepository = providerRepository;
        this.categoryRepository = categoryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAllWithCategoryAndProvider();
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        product.setActive(true);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado o no activo."));

        // 1. Actualizar campos básicos
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setActive(productDetails.getIsActive());

        // 2. Actualizar la categoría si se proporcionó una nueva
        if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
            Category category = categoryRepository.findById(productDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // 3. Actualizar el proveedor si se proporcionó uno nuevo
        if (productDetails.getProvider() != null && productDetails.getProvider().getId() != null) {
            Provider provider = providerRepository.findById(productDetails.getProvider().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));
            product.setProvider(provider);
        } else {
            product.setProvider(null);
        }

        return productRepository.save(product);
    }

    // Metodo para eliminar logicamente un producto
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado."));
        product.setActive(false);
        productRepository.save(product);
    }


    // Mtodo para el dashboard: obtener el total de productos
    public long countAllProducts() {
        return productRepository.count();
    }
    // Mtodo para el dashboard: obtener productos con bajo stock
    public List<Product> getProductsWithLowStock(int threshold) {
        return productRepository.findByStockLessThan(threshold);
    }
    // Mtodo para el dashboard: calcular el valor total del inventario
    public Double calculateTotalInventoryValue() {
        return productRepository.calculateTotalInventoryValue();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getTopSellingProducts() {
        return orderItemRepository.findTopSellingProducts();
    }
}