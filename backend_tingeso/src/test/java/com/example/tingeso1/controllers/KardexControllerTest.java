package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.services.KardexService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KardexController.class)
@ActiveProfiles("test")
public class KardexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KardexService kardexService;

    @Autowired
    private ObjectMapper objectMapper;

    private KardexEntity k1;
    private KardexEntity k2;

    @BeforeEach
    void setup() {
        k1 = new KardexEntity();
        k1.setIdKardex(1L);
        k1.setDateKardex(new Date());
        k1.setIdTool(10L);
        k1.setNameTool("Martillo");
        k1.setStateTool("PRESTAMO");

        k2 = new KardexEntity();
        k2.setIdKardex(2L);
        k2.setDateKardex(new Date());
        k2.setIdTool(11L);
        k2.setNameTool("Taladro");
        k2.setStateTool("DEVOLUCIÓN");
    }

    // ---------- GET /api/v1/kardex/ ----------
    @Test
    void whenListKardexes_thenReturnAllKardexes() throws Exception {
        when(kardexService.getKardexes()).thenReturn(new ArrayList<>(List.of(k1, k2)));

        mockMvc.perform(get("/api/v1/kardex/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameTool").value("Martillo"))
                .andExpect(jsonPath("$[1].stateTool").value("DEVOLUCIÓN"));
    }

    // ---------- GET /api/v1/kardex/id ----------
    @Test
    void whenGetKardexById_thenReturnKardex() throws Exception {
        when(kardexService.getKardexById(1L)).thenReturn(Optional.of(k1));

        mockMvc.perform(get("/api/v1/kardex/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTool").value("Martillo"))
                .andExpect(jsonPath("$.stateTool").value("PRESTAMO"));
    }


    // ---------- POST /api/v1/kardex/ ----------
    @Test
    void whenSaveKardex_thenReturnSavedKardex() throws Exception {
        when(kardexService.createKardex(any(KardexEntity.class))).thenReturn(k1);

        mockMvc.perform(post("/api/v1/kardex/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTool").value("Martillo"))
                .andExpect(jsonPath("$.stateTool").value("PRESTAMO"));
    }

    // ---------- PUT /api/v1/kardex/ ----------
    @Test
    void whenUpdateKardex_thenReturnUpdatedKardex() throws Exception {
        k1.setStateTool("BAJA");
        when(kardexService.updateKardex(any(KardexEntity.class))).thenReturn(k1);

        mockMvc.perform(put("/api/v1/kardex/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateTool").value("BAJA"));
    }

    // ---------- DELETE /api/v1/kardex/{id} ----------
    @Test
    void whenDeleteKardex_thenReturnNoContent() throws Exception {
        when(kardexService.deleteKardex(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/kardex/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
