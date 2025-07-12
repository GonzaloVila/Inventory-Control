package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;

import com.example.demo.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
    }

    @Test
    void whenCreateClient_withNonExistingEmail_thenClientIsSaved() {
        // Arrange
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        Client savedClient = clientService.createClient(client);

        // Assert
        assertNotNull(savedClient);
        assertEquals("john.doe@example.com", savedClient.getEmail());
        verify(clientRepository).findByEmail(client.getEmail());
        verify(clientRepository).save(client);
    }

    @Test
    void whenCreateClient_withExistingEmail_thenThrowDuplicateResourceException() {
        // Arrange
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(client));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            clientService.createClient(client);
        });
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void whenGetAllClients_thenReturnsClientList() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(List.of(client));

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients);
        assertEquals(1, clients.size());
        verify(clientRepository).findAll();
    }

    @Test
    void whenGetClientById_withExistingId_thenReturnsClient() {
        // Arrange
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);

        // Act
        Optional<Client> foundClient = clientService.getClientById(1L);

        // Assert
        assertTrue(foundClient.isPresent());
        assertEquals(1L, foundClient.get().getId());
        verify(clientRepository).existsById(1L);
        verify(clientRepository).getReferenceById(1L);
    }

    @Test
    void whenGetClientById_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(clientRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(99L);
        });
        verify(clientRepository, never()).getReferenceById(anyLong());
    }

    @Test
    void whenUpdateClient_withExistingId_thenReturnsUpdatedClient() {
        // Arrange
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        Client updatedClient = clientService.updateClient(client);

        // Assert
        assertNotNull(updatedClient);
        verify(clientRepository).existsById(1L);
        verify(clientRepository).save(client);
    }

    @Test
    void whenUpdateClient_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(clientRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.updateClient(client);
        });
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void whenDeleteClient_withExistingId_thenDeletesClient() {
        // Arrange
        when(clientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            clientService.deleteClient(1L);
        });
        verify(clientRepository).existsById(1L);
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void whenDeleteClient_withNonExistingId_thenThrowResourceNotFoundException() {
        // Arrange
        when(clientRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.deleteClient(99L);
        });
        verify(clientRepository, never()).deleteById(anyLong());
    }
}
