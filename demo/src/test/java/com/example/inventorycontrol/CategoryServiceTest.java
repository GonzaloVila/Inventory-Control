package com.example.inventorycontrol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.example.inventorycontrol.exception.DuplicateResourceException;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Category;
import com.example.inventorycontrol.repository.CategoryRepository;
import com.example.inventorycontrol.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        // Inicializamos una categoría de ejemplo para no repetir código
        category = new Category();
        category.setId(1L);
        category.setName("Electrónica");
        category.setDescription("Dispositivos electrónicos y accesorios");
    }

    @Test
    void testGetAllCategorys() {
        // Arrange (Organizar)
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // Act (Actuar)
        List<Category> result = categoryService.getAllCategorys();

        // Assert (Afirmar)
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electrónica", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll(); // Verifica que se llamó al método 1 vez
    }

    @Test
    void testCreateCategory_Successful() {
        // Arrange
        when(categoryRepository.existsByName(anyString())).thenReturn(false); // Simulamos que el nombre NO existe
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category result = categoryService.createCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals("Electrónica", result.getName());
        verify(categoryRepository).existsByName("Electrónica"); // Verificamos la llamada con el nombre exacto
        verify(categoryRepository).save(category); // Verificamos que se intentó guardar
    }

    @Test
    void testCreateCategory_ExceptionLaunchesInDuplicate() {
        // Arrange
        when(categoryRepository.existsByName("Electrónica")).thenReturn(true); // Simulamos que el nombre YA existe

        // Act & Assert
        // Verificamos que se lanza la excepción correcta
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            categoryService.createCategory(category);
        });

        assertEquals("Ya existe una categoría con el nombre: Electrónica", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class)); // Verificamos que NUNCA se llamó a save()
    }

    @Test
    void testGetCategoryById_Found() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> result = categoryService.getCategoryById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Electrónica", result.get().getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Arrange
        when(categoryRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(99L);
        });
        verify(categoryRepository, never()).findById(anyLong()); // No debería llamar a findById si la existencia falla
    }

    @Test
    void testUpdateCategory_Successful() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category result = categoryService.updateCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository).save(category);
    }

    @Test
    void testUpdateCategory_NoTExist() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(category);
        });
        verify(categoryRepository, never()).save(any(Category.class));
    }


    @Test
    void testDeleteCategory_Successful() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L); // Para métodos void

        // Act & Assert
        // Verificamos que no se lanza ninguna excepción
        assertDoesNotThrow(() -> {
            categoryService.deleteCategory(1L);
        });

        verify(categoryRepository).deleteById(1L); // Verificamos que se llamó a deleteById
    }

    @Test
    void testDeleteCategory_NotExist() {
        // Arrange
        when(categoryRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(99L);
        });

        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
