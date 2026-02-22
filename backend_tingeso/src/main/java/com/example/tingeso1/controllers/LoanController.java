package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.tingeso1.entities.ToolEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService; 
    }

    // Get all loans
    @GetMapping("/")
    public ResponseEntity<List<LoanEntity>> listLoan() {
        List<LoanEntity> loans = loanService.getLoans();
        return ResponseEntity.ok(loans);
    }

    // Get loan by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<LoanEntity>> getLoanById(@PathVariable Long id) {
        Optional<LoanEntity> loan = loanService.findById(id);
        return ResponseEntity.ok(loan);
    }

    // Create loan
    @PostMapping("/")
    public ResponseEntity<java.util.Map<String, Object>> saveLoan(@RequestBody LoanEntity loan) {
        try {
            LoanEntity newloan = loanService.createLoan(loan);
            // Respuesta exitosa con código 200
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("success", true);
            response.put("data", newloan);
            response.put("message", "Préstamo creado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Respuesta con código 400 Bad Request para errores
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("success", false);
            response.put("data", null);
            response.put("message", e.getMessage() != null ? e.getMessage() : "Error al crear préstamo");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update loan
    @PutMapping("/")
    public ResponseEntity<LoanEntity> updateLoan(@RequestBody LoanEntity loan) {
        LoanEntity loanUpdated = loanService.updateLoan(loan);
        return ResponseEntity.ok(loanUpdated);
    }

    // Get loans by rut
    @GetMapping("/by-rut/{rut}")
    public ResponseEntity<List<LoanEntity>> getLoansByRut(@PathVariable String rut) {
        return ResponseEntity.ok(loanService.findByRutClient(rut));
    }

    // Delete loan by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLoanByID(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    // Get tools by loan id
    @GetMapping("/tools-by-loan/{id}")
    public ResponseEntity<List<ToolEntity>> getToolsByLoanId(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getToolsByLoanId(id));
    }

    // Update penalty loan
    @PutMapping("/update-penalty/{id}")
    public ResponseEntity<LoanEntity> updatePenaltyLoan(@PathVariable Long id) {
        LoanEntity loanUpdated = loanService.updatePenaltyLoan(id);
        return ResponseEntity.ok(loanUpdated);
    }

    // Finish loan
    @PutMapping("/finish-loan/{id}/{totalValue}")
    public ResponseEntity<LoanEntity> finishLoan(@PathVariable Long id, @PathVariable Integer totalValue) {
        return ResponseEntity.ok(loanService.finalizeLoan(id, totalValue));
    }

    // Get loans by range date
    @GetMapping("/loans-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<List<LoanEntity>> getLoansByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        return ResponseEntity.ok(loanService.getLoansByDateRange(initDate, endDate));
    }

    // Get num loans "RESTRINGIDO"
    @GetMapping("/num-loans-restringido/{rut}")
    public ResponseEntity<Integer> getNumLoansRestringido(@PathVariable String rut) {
        try {
            return ResponseEntity.ok(loanService.getNumLoanRestrinByRutClient(rut));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    // Get num loans "ACTIVO"
    @GetMapping("/num-active-loans/{rut}")
    public ResponseEntity<Integer> getNumActiveLoans(@PathVariable String rut) {
        try {
            return ResponseEntity.ok(loanService.getNumActiveLoans(rut));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }
}
