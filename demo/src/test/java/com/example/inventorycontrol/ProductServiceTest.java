package com.example.inventorycontrol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Category;
import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.ProductRepository;
import com.example.inventorycontrol.service.ProductService;

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
    private Category category;
    private Provider provider;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics", "Devices for home and office");

        provider = new Provider(1L, "TechCorp", "123 Main St", "555-1234", "info@techcorp.com");

        product = new Product(1L, category, provider, "Laptop", "Powerful laptop", 10, 1200.0);
        product.setActive(true); // Asegurarse de que esté activo por defecto para las pruebas
    }

    @Test
    void whenCreateProduct_withValidData_thenProductIsSavedAndActive() {
        // Arrange
        Product newProduct = new Product(null, category, provider, "New Keyboard", "Mechanical keyboard", 50, 75.0);

        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // Act
        Product savedProduct = productService.createProduct(newProduct);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("New Keyboard", savedProduct.getName());
        assertTrue(savedProduct.isActive()); // Verifica que está activo
        verify(productRepository).save(newProduct);
    }

    @Test
    void whenCreateProduct_withNullName_thenNoActionTaken() {
        Product productWithNullName = new Product(null, category, provider, null, "Description", 10, 100.0);

        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productWithNullName);
        });
        verify(productRepository, never()).save(any(Product.class));
    }


    @Test
    void whenGetAllProducts_thenReturnsProductList() {
        // Arrange
        // product ya está activo en setUp()
        when(productRepository.findAll()).thenReturn(List.of(product));

        // Act
        List<Product> products = productService.getAllProducts();

        // Assert
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Laptop", products.get(0).getName());
        assertTrue(products.get(0).isActive());
        verify(productRepository).findAll();
    }

    @Test
    void whenGetProductById_withExistingId_thenReturnsProduct() {
        // Arrange
        Long productId = product.getId(); // Usamos el ID del producto de setUp
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> foundProduct = productService.getProductById(productId);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(productId, foundProduct.get().getId());
        assertEquals("Laptop", foundProduct.get().getName());
        assertTrue(foundProduct.get().isActive());
        verify(productRepository).findById(productId);
    }

    @Test
    void whenGetProductById_withNonExistingId_thenReturnsEmptyOptional() {
        // Arrange
        Long nonExistingId = 99L;
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productService.getProductById(nonExistingId);

        // Assert
        assertFalse(foundProduct.isPresent());
        verify(productRepository).findById(nonExistingId);
    }


    @Test
    void whenUpdateProduct_withExistingId_thenReturnsUpdatedProduct() {
        // Arrange
        Long productId = product.getId();
        // Crear un objeto con los nuevos detalles para la actualización
        Product updatedDetails = new Product(productId, category, provider, "Updated Laptop Pro", "New description", 15, 1500.0);
        updatedDetails.setActive(true); // El producto actualizado sigue activo

        // Cuando se intente buscar el producto original por ID, devolverlo (activo)
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        // Cuando se intente guardar cualquier producto (el ya actualizado), devolverlo
        when(productRepository.save(any(Product.class))).thenReturn(updatedDetails);

        // Act

        Product resultProduct = productService.updateProduct(productId, updatedDetails);

        // Assert
        assertNotNull(resultProduct);
        assertEquals(productId, resultProduct.getId());
        assertEquals("Updated Laptop Pro", resultProduct.getName());
        assertEquals(1500.0, resultProduct.getPrice());
        assertEquals(15, resultProduct.getStock());
        assertTrue(resultProduct.isActive());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void whenUpdateProduct_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        Long nonExistingId = 99L;
        // Crear un objeto con detalles de actualización (el ID en el objeto no importa tanto para este caso)
        Product updateAttempt = new Product(nonExistingId, category, provider, "Phantom Product", "Ghost", 1, 1.0);

        // Cuando se busque por el ID no existente, devolver Optional.empty()
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(nonExistingId, updateAttempt); // Pasa ambos argumentos
        });

        assertEquals("Producto con ID " + nonExistingId + " no encontrado o no activo.", thrown.getMessage());
        verify(productRepository, times(1)).findById(nonExistingId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void whenDeleteProduct_withExistingId_thenProductIsMarkedInactive() {
        // Arrange
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        // Act & Assert
        assertDoesNotThrow(() -> {
            productService.deleteProduct(productId);
        });
        verify(productRepository, times(1)).deleteById(productId);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void whenDeleteProduct_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        Long nonExistingId = 99L;
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(nonExistingId);
        });
        verify(productRepository, never()).deleteById(anyLong());
    }
}