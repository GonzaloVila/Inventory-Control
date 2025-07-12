package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;

import com.example.demo.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderService providerService;

    private Provider provider;

    @BeforeEach
    void setUp() {
        provider = new Provider();
        provider.setId(1L);
        provider.setName("TechCorp");
        provider.setEmail("contact@techcorp.com");
    }

    @Test
    void whenCreateProvider_withNonExistingEmail_thenProviderIsSaved() {
        // Arrange
        when(providerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        // Act
        Provider savedProvider = providerService.createProvider(provider);

        // Assert
        assertNotNull(savedProvider);
        assertEquals("contact@techcorp.com", savedProvider.getEmail());
        verify(providerRepository).save(provider);
    }

    @Test
    void whenCreateProvider_withExistingEmail_thenThrowDuplicateResourceException() {
        // Arrange
        when(providerRepository.findByEmail(anyString())).thenReturn(Optional.of(provider));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            providerService.createProvider(provider);
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
        verify(providerRepository).findAll();
    }

    @Test
    void whenGetProviderById_withExistingId_thenReturnsProvider() {
        // Arrange
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(providerRepository.getReferenceById(1L)).thenReturn(provider);

        // Act
        Optional<Provider> foundProvider = providerService.getProviderById(1L);

        // Assert
        assertTrue(foundProvider.isPresent());
        assertEquals(1L, foundProvider.get().getId());
    }

    @Test
    void whenGetProviderById_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(providerRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.getProviderById(99L);
        });
    }

    @Test
    void whenUpdateProvider_withExistingId_thenReturnsUpdatedProvider() {
        // Arrange
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        // Act
        Provider updatedProvider = providerService.updateProvider(provider);

        // Assert
        assertNotNull(updatedProvider);
        verify(providerRepository).save(provider);
    }

    @Test
    void whenUpdateProvider_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(providerRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.updateProvider(provider);
        });
    }

    @Test
    void whenDeleteProvider_withExistingId_thenDeletesProvider() {
        // Arrange
        when(providerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(providerRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            providerService.deleteProvider(1L);
        });
        verify(providerRepository).deleteById(1L);
    }

    @Test
    void whenDeleteProvider_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(providerRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.deleteProvider(99L);
        });
    }
}
