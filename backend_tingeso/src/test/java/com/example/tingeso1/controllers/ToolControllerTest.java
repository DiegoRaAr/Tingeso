package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.services.ToolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToolController.class)
@ActiveProfiles("test")
public class ToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToolService toolService;

    @Autowired
    private ObjectMapper objectMapper;

    private ToolEntity t1;
    private ToolEntity t2;

    @BeforeEach
    void setup() {
        t1 = new ToolEntity(1L, "Martillo", "Manual", 10000, "ACTIVO", 5, 5000, 2000, 3000);
        t2 = new ToolEntity(2L, "Taladro", "Eléctrica", 15000, "ACTIVO", 2, 7000, 2500, 4000);
    }

    // ---------- GET /api/v1/tool/ ----------
    @Test
    void whenListTools_thenReturnAllTools() throws Exception {
        when(toolService.getTools()).thenReturn(new ArrayList<>(Arrays.asList(t1, t2)));

        mockMvc.perform(get("/api/v1/tool/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameTool").value("Martillo"))
                .andExpect(jsonPath("$[1].nameTool").value("Taladro"));
    }

    // ---------- GET /api/v1/tool/{id} ----------
    @Test
    void whenGetToolById_thenReturnTool() throws Exception {
        when(toolService.findById(1L)).thenReturn(t1);

        mockMvc.perform(get("/api/v1/tool/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTool").value("Martillo"))
                .andExpect(jsonPath("$.categoryTool").value("Manual"));
    }

    // ---------- POST /api/v1/tool/ ----------
    @Test
    void whenSaveTool_thenReturnCreatedTool() throws Exception {
        when(toolService.createTool(any(ToolEntity.class))).thenReturn(t1);

        mockMvc.perform(post("/api/v1/tool/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTool").value("Martillo"))
                .andExpect(jsonPath("$.stateTool").value("ACTIVO"));
    }

    // ---------- PUT /api/v1/tool/ ----------
    @Test
    void whenUpdateTool_thenReturnUpdatedTool() throws Exception {
        t1.setStockTool(10);
        when(toolService.updateTool(any(ToolEntity.class))).thenReturn(t1);

        mockMvc.perform(put("/api/v1/tool/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockTool").value(10));
    }

    // ---------- DELETE /api/v1/tool/{id} ----------
    @Test
    void whenDeleteTool_thenReturnNoContent() throws Exception {
        when(toolService.deleteTool(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tool/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ---------- PUT /api/v1/tool/subtract-tool/{id} ----------
    @Test
    void whenSubtractTool_thenReturnNoContent() throws Exception {
        when(toolService.subtractTool(1L)).thenReturn(true);

        mockMvc.perform(put("/api/v1/tool/subtract-tool/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ---------- PUT /api/v1/tool/add-tool/{id} ----------
    @Test
    void whenAddTool_thenReturnNoContent() throws Exception {
        when(toolService.addTool(1L)).thenReturn(true);

        mockMvc.perform(put("/api/v1/tool/add-tool/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ---------- GET /api/v1/tool/best-tools-by-range-date/{initDate}/{endDate} ----------
    @Test
    void whenGetBestToolsByRangeDate_thenReturnList() throws Exception {
        when(toolService.getBestToolsByRangeDate(any(Date.class), any(Date.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(t1, t2)));

        mockMvc.perform(get("/api/v1/tool/best-tools-by-range-date/2024-01-01/2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameTool").value("Martillo"))
                .andExpect(jsonPath("$[1].nameTool").value("Taladro"));
    }
}
