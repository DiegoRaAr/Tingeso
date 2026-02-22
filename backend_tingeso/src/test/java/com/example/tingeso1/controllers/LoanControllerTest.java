package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.services.AdminService;
import com.example.tingeso1.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@ActiveProfiles("test")
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoanEntity loan1;
    private LoanEntity loan2;
    private ToolEntity tool;

    @BeforeEach
    void setup() {
        loan1 = new LoanEntity();
        loan1.setIdLoan(1L);
        loan1.setStateLoan("ACTIVO");
        loan1.setPenaltyLoan(0);
        loan1.setInitDate(new Date());
        loan1.setEndDate(new Date());

        loan2 = new LoanEntity();
        loan2.setIdLoan(2L);
        loan2.setStateLoan("FINALIZADO");
        loan2.setPenaltyLoan(1000);
        loan2.setInitDate(new Date());
        loan2.setEndDate(new Date());

        tool = new ToolEntity();
        tool.setIdTool(10L);
        tool.setNameTool("Martillo");
    }

    // ---------- GET /api/v1/loan/ ----------
    @Test
    void whenListLoans_thenReturnAllLoans() throws Exception {
        when(loanService.getLoans()).thenReturn(new ArrayList<>(Arrays.asList(loan1, loan2)));


        mockMvc.perform(get("/api/v1/loan/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stateLoan").value("ACTIVO"))
                .andExpect(jsonPath("$[1].stateLoan").value("FINALIZADO"));
    }

    // ---------- GET /api/v1/loan/{id} ----------
    @Test
    void whenGetLoanById_thenReturnLoan() throws Exception {
        when(loanService.findById(1L)).thenReturn(Optional.of(loan1));

        mockMvc.perform(get("/api/v1/loan/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLoan").value(1))
                .andExpect(jsonPath("$.stateLoan").value("ACTIVO"));
    }

    // ---------- POST /api/v1/loan/ ----------
    @Test
    void whenSaveLoan_thenReturnSavedLoan() throws Exception {
        when(loanService.createLoan(any(LoanEntity.class))).thenReturn(loan1);

        mockMvc.perform(post("/api/v1/loan/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stateLoan").value("ACTIVO"));
    }

    // ---------- PUT /api/v1/loan/ ----------
    @Test
    void whenUpdateLoan_thenReturnUpdatedLoan() throws Exception {
        loan1.setPenaltyLoan(500);
        when(loanService.updateLoan(any(LoanEntity.class))).thenReturn(loan1);

        mockMvc.perform(put("/api/v1/loan/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.penaltyLoan").value(500));
    }

    // ---------- GET /api/v1/loan/by-rut/{rut} ----------
    @Test
    void whenGetLoansByRut_thenReturnList() throws Exception {
        when(loanService.findByRutClient("11-1")).thenReturn(List.of(loan1));

        mockMvc.perform(get("/api/v1/loan/by-rut/{rut}", "11-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stateLoan").value("ACTIVO"));
    }

    // ---------- DELETE /api/v1/loan/{id} ----------
    @Test
    void whenDeleteLoan_thenReturnNoContent() throws Exception {
        when(loanService.deleteLoan(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/loan/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ---------- GET /api/v1/loan/tools-by-loan/{id} ----------
    @Test
    void whenGetToolsByLoan_thenReturnToolList() throws Exception {
        when(loanService.getToolsByLoanId(1L)).thenReturn(List.of(tool));

        mockMvc.perform(get("/api/v1/loan/tools-by-loan/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameTool").value("Martillo"));
    }

    // ---------- PUT /api/v1/loan/update-penalty/{id} ----------
    @Test
    void whenUpdatePenaltyLoan_thenReturnUpdatedLoan() throws Exception {
        loan1.setPenaltyLoan(1500);
        when(loanService.updatePenaltyLoan(1L)).thenReturn(loan1);

        mockMvc.perform(put("/api/v1/loan/update-penalty/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.penaltyLoan").value(1500));
    }

    // ---------- PUT /api/v1/loan/finish-loan/{id}/{totalValue} ----------
    @Test
    void whenFinishLoan_thenReturnFinalizedLoan() throws Exception {
        loan1.setStateLoan("FINALIZADO");
        when(loanService.finalizeLoan(1L, 5000)).thenReturn(loan1);

        mockMvc.perform(put("/api/v1/loan/finish-loan/{id}/{totalValue}", 1L, 5000))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateLoan").value("FINALIZADO"));
    }

    // ---------- GET /api/v1/loan/loans-by-range-date/{initDate}/{endDate} ----------
    @Test
    void whenGetLoansByDateRange_thenReturnList() throws Exception {
        when(loanService.getLoansByDateRange(any(Date.class), any(Date.class)))
                .thenReturn(List.of(loan1, loan2));

        mockMvc.perform(get("/api/v1/loan/loans-by-range-date/2024-01-01/2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stateLoan").value("ACTIVO"));
    }
}
