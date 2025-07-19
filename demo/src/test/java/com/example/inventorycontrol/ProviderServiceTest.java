package com.example.inventorycontrol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.inventorycontrol.exception.DuplicateResourceException;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.repository.ProviderRepository;

import com.example.inventorycontrol.service.ProductService;
import com.example.inventorycontrol.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProviderService providerService;

    private Provider provider;

    @BeforeEach
    void setUp() {
        provider = new Provider(1L, "TechCorp", "123 Main St", "555-1234", "contact@techcorp.com");
    }

    @Test
    void whenCreateProvider_withNonExistingEmail_thenProviderIsSaved() {
        // Arrange
        Provider newProvider = new Provider(null, "New Corp", "456 Oak Ave", "111-222-3333", "new@corp.com");
        when(providerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(providerRepository.save(any(Provider.class))).thenReturn(newProvider); // Devolver el nuevo proveedor

        // Act
        Provider savedProvider = providerService.createProvider(newProvider); // Pasar el nuevo proveedor

        // Assert
        assertNotNull(savedProvider);
        assertEquals("new@corp.com", savedProvider.getEmail());
        verify(providerRepository).save(newProvider); // Verificar que se guardó el nuevo proveedor
    }

    @Test
    void whenCreateProvider_withExistingEmail_thenThrowDuplicateResourceException() {
        // Arrange
        // Usamos el 'provider' de setUp que tiene un email
        when(providerRepository.findByEmail(anyString())).thenReturn(Optional.of(provider));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            providerService.createProvider(provider); // Intentar crear con el mismo email
        });
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void whenGetAllProviders_thenReturnsProviderList() {
        // Arrange
        when(providerRepository.findAll()).thenReturn(List.of(provider));

        // Act
        List<Provider> providers = providerService.getAllProviders();

        // Assert
        assertNotNull(providers);
        assertEquals(1, providers.size());
        assertEquals("TechCorp", providers.get(0).getName()); // Añadir una aserción de contenido
        verify(providerRepository).findAll();
    }

    @Test
    void whenGetProviderById_withExistingId_thenReturnsProvider() {
        // Arrange
        Long providerId = provider.getId(); // Usamos el ID del provider de setUp
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(provider));

        // Act
        Optional<Provider> foundProvider = providerService.getProviderById(providerId);

        // Assert
        assertTrue(foundProvider.isPresent());
        assertEquals(providerId, foundProvider.get().getId());
        assertEquals("TechCorp", foundProvider.get().getName()); // Verificar el nombre también
        verify(providerRepository).findById(providerId); // Verificar que findById fue llamado
    }

    @Test
    void whenGetProviderById_withNonExistingId_thenReturnsEmptyOptional() {
        // Arrange
        Long nonExistingId = 99L;
        when(providerRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Optional<Provider> foundProvider = providerService.getProviderById(nonExistingId);

        // Assert
        assertFalse(foundProvider.isPresent()); // El servicio devuelve Optional.empty()
        verify(providerRepository).findById(nonExistingId);
    }

    @Test
    void whenUpdateProvider_withExistingId_thenReturnsUpdatedProvider() {
        // Arrange
        Long providerId = provider.getId(); // ID del proveedor a actualizar
        // Crear un objeto con los nuevos detalles para la actualización
        Provider updatedDetails = new Provider(providerId, "UpdatedCorp", "456 New St", "999-888-7777", "updated@corp.com");

        when(providerRepository.findById(providerId)).thenReturn(Optional.of(provider)); // Devuelve el proveedor original

        // Cuando se guarde cualquier instancia de Provider, devolver la instancia con los detalles actualizados
        when(providerRepository.save(any(Provider.class))).thenReturn(updatedDetails);

        // Act
        Provider resultProvider = providerService.updateProvider(providerId, updatedDetails);

        // Assert
        assertNotNull(resultProvider);
        assertEquals(providerId, resultProvider.getId());
        assertEquals("UpdatedCorp", resultProvider.getName()); // Verificar que el nombre se actualizó
        assertEquals("456 New St", resultProvider.getAddress()); // Verificar otras actualizaciones
        verify(providerRepository, times(1)).findById(providerId); // Se verifica la búsqueda
        verify(providerRepository, times(1)).save(any(Provider.class)); // Se verifica el guardado
    }

    @Test
    void whenUpdateProvider_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        Long nonExistingId = 99L;
        // Crear un objeto dummy para los detalles de actualización
        Provider updateAttempt = new Provider(nonExistingId, "Dummy", "Dummy", "Dummy", "dummy@example.com");

        when(providerRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.updateProvider(nonExistingId, updateAttempt);
        });
        verify(providerRepository, times(1)).findById(nonExistingId);
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void whenDeleteProvider_withExistingId_thenDeletesProviderAndSoftDeletesProducts() {
        // Arrange
        Long providerId = 1L;
        // Crear algunos productos asociados al proveedor
        Product product1 = new Product(101L, null, provider, "Product A", "Desc A", 5, 10.0);
        Product product2 = new Product(102L, null, provider, "Product B", "Desc B", 3, 20.0);
        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);
        provider.setProducts(products); // Asignar productos al proveedor

        // *** CORRECCIÓN CRÍTICA AQUÍ: Mockear findById para obtener el proveedor ***
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(provider));

        // Mockear las llamadas a productService.deleteProduct para cada producto
        // Asumimos que productService.deleteProduct es el método de delete
        doNothing().when(productService).deleteProduct(anyLong());

        // Mockear la eliminación física del proveedor
        doNothing().when(providerRepository).delete(provider);

        // Act & Assert
        assertDoesNotThrow(() -> {
            providerService.deleteProvider(providerId);
        });

        // Verificaciones
        verify(providerRepository, times(1)).findById(providerId); // Se buscó el proveedor
        verify(productService, times(1)).deleteProduct(product1.getId()); // Se "soft" eliminó el producto 1
        verify(productService, times(1)).deleteProduct(product2.getId()); // Se "soft"eliminó el producto 2
        verify(providerRepository, times(1)).delete(provider); // Se eliminó físicamente el proveedor
    }

    @Test
    void whenDeleteProvider_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        Long nonExistingId = 99L;
        when(providerRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.deleteProvider(nonExistingId);
        });
        verify(providerRepository, times(1)).findById(nonExistingId); // Se verifica la búsqueda
        verify(productService, never()).deleteProduct(anyLong()); // No se intentan eliminar productos
        verify(providerRepository, never()).delete(any(Provider.class)); // No se elimina el proveedor
    }
}