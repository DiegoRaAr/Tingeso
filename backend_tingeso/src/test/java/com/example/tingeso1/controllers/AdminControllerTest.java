package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.AdminEntity;
import com.example.tingeso1.services.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(AdminController.class)
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    private AdminEntity a1;
    private AdminEntity a2;

    @BeforeEach
    void setup() {
        a1 = new AdminEntity();
        a1.setIdAdmin(1L);
        a1.setNameAdmin("Diego Ramos");
        a1.setRutAdmin("11-1");
        a1.setStateAdmin("ACTIVO");
        a1.setPasswordAdmin("1234");

        a2 = new AdminEntity();
        a2.setIdAdmin(2L);
        a2.setNameAdmin("Andrea Soto");
        a2.setRutAdmin("22-2");
        a2.setStateAdmin("INACTIVO");
        a2.setPasswordAdmin("abcd");
    }

    // ---------- GET /api/v1/admin/ ----------
    @Test
    void whenListAdmins_thenReturnListOfAdmins() throws Exception {
        when(adminService.getAdmins()).thenReturn(new java.util.ArrayList<>(List.of(a1, a2)));

        mockMvc.perform(get("/api/v1/admin/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameAdmin").value("Diego Ramos"))
                .andExpect(jsonPath("$[1].nameAdmin").value("Andrea Soto"));
    }

    // ---------- GET /api/v1/admin/{id} ----------
    @Test
    void whenGetAdminById_thenReturnAdmin() throws Exception {
        when(adminService.getAdminById(1L)).thenReturn(a1);

        mockMvc.perform(get("/api/v1/admin/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameAdmin").value("Diego Ramos"))
                .andExpect(jsonPath("$.rutAdmin").value("11-1"));
    }

    // ---------- POST /api/v1/admin/ ----------
    @Test
    void whenSaveAdmin_thenReturnCreatedAdmin() throws Exception {
        when(adminService.saveAdmin(any(AdminEntity.class))).thenReturn(a1);

        mockMvc.perform(post("/api/v1/admin/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameAdmin").value("Diego Ramos"))
                .andExpect(jsonPath("$.stateAdmin").value("ACTIVO"));
    }

    // ---------- PUT /api/v1/admin/ ----------
    @Test
    void whenUpdateAdmin_thenReturnUpdatedAdmin() throws Exception {
        a1.setStateAdmin("INACTIVO");
        when(adminService.updateAdmin(any(AdminEntity.class))).thenReturn(a1);

        mockMvc.perform(put("/api/v1/admin/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateAdmin").value("INACTIVO"));
    }

    // ---------- DELETE /api/v1/admin/{id} ----------
    @Test
    void whenDeleteAdmin_thenReturnNoContent() throws Exception {
        when(adminService.deleteAdmin(1L)).thenReturn(true);


        mockMvc.perform(delete("/api/v1/admin/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
