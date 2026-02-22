package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.services.EmployeeService;
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

@WebMvcTest(EmployeeController.class)
@ActiveProfiles("test")
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeEntity e1;
    private EmployeeEntity e2;

    @BeforeEach
    void setup() {
        e1 = new EmployeeEntity(1L, "11-1", "Diego Ramos", "ACTIVO", "1234");
        e2 = new EmployeeEntity(2L, "22-2", "Andrea Soto", "INACTIVO", "abcd");
    }

    // ---------- GET /api/v1/employee/ ----------
    @Test
    void whenListEmployees_thenReturnAllEmployees() throws Exception {
        when(employeeService.getEmployees()).thenReturn(new ArrayList<>(List.of(e1, e2)));

        mockMvc.perform(get("/api/v1/employee/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameEmployee").value("Diego Ramos"))
                .andExpect(jsonPath("$[1].stateEmployee").value("INACTIVO"));
    }

    // ---------- GET /api/v1/employee/{id} ----------
    @Test
    void whenGetEmployeeById_thenReturnEmployee() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(e1));

        mockMvc.perform(get("/api/v1/employee/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rutEmployee").value("11-1"))
                .andExpect(jsonPath("$.nameEmployee").value("Diego Ramos"));
    }

    // ---------- POST /api/v1/employee/ ----------
    @Test
    void whenSaveEmployee_thenReturnSavedEmployee() throws Exception {
        when(employeeService.saveEmployee(any(EmployeeEntity.class))).thenReturn(e1);

        mockMvc.perform(post("/api/v1/employee/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameEmployee").value("Diego Ramos"))
                .andExpect(jsonPath("$.stateEmployee").value("ACTIVO"));
    }

    // ---------- PUT /api/v1/employee/ ----------
    @Test
    void whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        e1.setStateEmployee("INACTIVO");
        when(employeeService.updateEmployee(any(EmployeeEntity.class))).thenReturn(e1);

        mockMvc.perform(put("/api/v1/employee/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateEmployee").value("INACTIVO"));
    }

    // ---------- DELETE /api/v1/employee/{id} ----------
    @Test
    void whenDeleteEmployee_thenReturnNoContent() throws Exception {
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/employee/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
