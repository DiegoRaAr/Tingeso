package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientEntity c1;
    private ClientEntity c2;

    @BeforeEach
    void setup() {
        c1 = new ClientEntity(1L, "11-1", "Diego Ramos", "ACTIVO", "diego@correo.com", "987654321");
        c2 = new ClientEntity(2L, "22-2", "Andrea Soto", "RESTRINGIDO", "andrea@correo.com", "912345678");
    }

    // ---------- GET /api/v1/client/ ----------
    @Test
    void whenListClients_thenReturnAllClients() throws Exception {
        when(clientService.getClients()).thenReturn(new ArrayList<>(List.of(c1, c2)));

        mockMvc.perform(get("/api/v1/client/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameClient").value("Diego Ramos"))
                .andExpect(jsonPath("$[1].stateClient").value("RESTRINGIDO"));
    }

    // ---------- GET /api/v1/client/{id} ----------
    @Test
    void whenGetClientById_thenReturnClient() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(c1));

        mockMvc.perform(get("/api/v1/client/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rutClient").value("11-1"))
                .andExpect(jsonPath("$.nameClient").value("Diego Ramos"));
    }

    // ---------- POST /api/v1/client/ ----------
    @Test
    void whenSaveClient_thenReturnSavedClient() throws Exception {
        when(clientService.saveClient(any(ClientEntity.class))).thenReturn(c1);

        mockMvc.perform(post("/api/v1/client/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameClient").value("Diego Ramos"))
                .andExpect(jsonPath("$.emailClient").value("diego@correo.com"));
    }

    // ---------- PUT /api/v1/client/ ----------
    @Test
    void whenUpdateClient_thenReturnUpdatedClient() throws Exception {
        c1.setStateClient("RESTRINGIDO");
        when(clientService.updateClient(any(ClientEntity.class))).thenReturn(c1);

        mockMvc.perform(put("/api/v1/client/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateClient").value("RESTRINGIDO"));
    }

    // ---------- DELETE /api/v1/client/{id} ----------
    @Test
    void whenDeleteClient_thenReturnNoContent() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/client/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ---------- GET /api/v1/client/by-rut/{rut} ----------
    @Test
    void whenGetClientByRut_thenReturnClient() throws Exception {
        when(clientService.getClientByRut("11-1")).thenReturn(c1);

        mockMvc.perform(get("/api/v1/client/by-rut/{rut}", "11-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameClient").value("Diego Ramos"));
    }

    // ---------- PUT /api/v1/client/change-state/{id} ----------
    @Test
    void whenChangeStateClient_thenReturnUpdatedState() throws Exception {
        c1.setStateClient("RESTRINGIDO");
        when(clientService.changeStateClient(1L)).thenReturn(c1);

        mockMvc.perform(put("/api/v1/client/change-state/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateClient").value("RESTRINGIDO"));
    }

    // ---------- GET /api/v1/client/restricted-clients ----------
    @Test
    void whenGetRestrictedClients_thenReturnRestrictedList() throws Exception {
        when(clientService.getRestrictedClients()).thenReturn(new ArrayList<>(List.of(c2)));

        mockMvc.perform(get("/api/v1/client/restricted-clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stateClient").value("RESTRINGIDO"));
    }
}
