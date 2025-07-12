package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.Provider;
import com.example.demo.repository.ProductRepository;

import com.example.demo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setProvider(new Provider()); // Assume valid provider
    }

    @Test
    void whenCreateProduct_withValidData_thenProductIsSaved() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product savedProduct = productService.createProduct(product);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getName());
        verify(productRepository).save(product);
    }

    @Test
    void whenCreateProduct_withNullName_thenThrowIllegalArgumentException() {
        // Arrange
        product.setName(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(product);
        });
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void whenGetAllProducts_thenReturnsProductList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(product));

        // Act
        List<Product> products = productService.getAllProducts();

        // Assert
        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository).findAll();
    }

    @Test
    void whenGetProductById_withExistingId_thenReturnsProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.getReferenceById(1L)).thenReturn(product);

        // Act
        Optional<Product> foundProduct = productService.getProductById(1L);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(1L, foundProduct.get().getId());
    }

    @Test
    void whenGetProductById_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(productRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });
    }

    @Test
    void whenUpdateProduct_withExistingId_thenReturnsUpdatedProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product updatedProduct = productService.updateProduct(product);

        // Assert
        assertNotNull(updatedProduct);
        verify(productRepository).save(product);
    }

    @Test
    void whenUpdateProduct_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(productRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(product);
        });
    }

    @Test
    void whenDeleteProduct_withExistingId_thenDeletesProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            productService.deleteProduct(1L);
        });
        verify(productRepository).deleteById(1L);
    }

    @Test
    void whenDeleteProduct_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(productRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(99L);
        });
    }
}
