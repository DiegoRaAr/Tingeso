package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientEntity client1;
    private ClientEntity client2;

    @BeforeEach
    void setup() {
        client1 = new ClientEntity();
        client1.setIdClient(1L);
        client1.setNameClient("Juan Pérez");
        client1.setRutClient("11-1");
        client1.setStateClient("ACTIVO");

        client2 = new ClientEntity();
        client2.setIdClient(2L);
        client2.setNameClient("Ana Gómez");
        client2.setRutClient("22-2");
        client2.setStateClient("RESTRINGIDO");
    }

    // ---------- getClients ----------
    @Test
    void whenGetClients_thenReturnAllClients() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>(List.of(client1, client2)));

        ArrayList<ClientEntity> result = clientService.getClients();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameClient()).isEqualTo("Juan Pérez");
        verify(clientRepository, times(1)).findAll();
    }

    // ---------- saveClient ----------
    @Test
    void whenSaveClient_thenReturnSavedClient() {
        when(clientRepository.save(client1)).thenReturn(client1);

        ClientEntity result = clientService.saveClient(client1);

        assertThat(result).isEqualTo(client1);
        verify(clientRepository, times(1)).save(client1);
    }

    // ---------- getClientById ----------
    @Test
    void whenGetClientById_thenReturnClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));

        Optional<ClientEntity> result = clientService.getClientById(1L);

        assertThat(result.get().getRutClient()).isEqualTo("11-1");
        verify(clientRepository, times(1)).findById(1L);
    }

    // ---------- getClientByRut ----------
    @Test
    void whenGetClientByRut_thenReturnClient() {
        when(clientRepository.findByRutClient("11-1")).thenReturn(client1);

        ClientEntity result = clientService.getClientByRut("11-1");

        assertThat(result.getNameClient()).isEqualTo("Juan Pérez");
        verify(clientRepository, times(1)).findByRutClient("11-1");
    }

    // ---------- updateClient ----------
    @Test
    void whenUpdateClient_thenReturnUpdatedClient() {
        client1.setNameClient("Juan Actualizado");
        when(clientRepository.save(client1)).thenReturn(client1);

        ClientEntity result = clientService.updateClient(client1);

        assertThat(result.getNameClient()).isEqualTo("Juan Actualizado");
        verify(clientRepository, times(1)).save(client1);
    }

    // ---------- deleteClient ----------
    @Test
    void whenDeleteClient_thenReturnTrue() throws Exception {
        doNothing().when(clientRepository).deleteById(1L);

        boolean result = clientService.deleteClient(1L);

        assertThat(result).isTrue();
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteClientFails_thenThrowException() {
        doThrow(new RuntimeException("DB error")).when(clientRepository).deleteById(1L);

        Exception e = assertThrows(Exception.class, () -> clientService.deleteClient(1L));

        assertThat(e.getMessage()).contains("Error deleting client with id: 1");
        verify(clientRepository, times(1)).deleteById(1L);
    }

    // ---------- changeStateClient ----------
    @Test
    void whenClientIsActive_thenChangeToRestricted() throws Exception {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientEntity result = clientService.changeStateClient(1L);

        assertThat(result.getStateClient()).isEqualTo("RESTRINGIDO");
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void whenClientIsRestricted_thenChangeToActive() throws Exception {
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client2));
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientEntity result = clientService.changeStateClient(2L);

        assertThat(result.getStateClient()).isEqualTo("ACTIVO");
        verify(clientRepository, times(1)).save(client2);
    }

    // ---------- getRestrictedClients ----------
    @Test
    void whenGetRestrictedClients_thenReturnOnlyRestrictedOnes() {
        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        ArrayList<ClientEntity> result = clientService.getRestrictedClients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStateClient()).isEqualTo("RESTRINGIDO");
        verify(clientRepository, times(1)).findAll();
    }
}
