package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.services.AdminService;
import com.example.tingeso1.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tingeso1.entities.ToolEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController {
    @Autowired
    LoanService loanService;

    // Get all loans
    @GetMapping("/")
    public ResponseEntity<List<LoanEntity>> listLoan(){
        List<LoanEntity> loans = loanService.getLoans();
        return ResponseEntity.ok(loans);
    }

    // Get loan by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<LoanEntity>> getLoanById(@PathVariable Long id){
        Optional<LoanEntity> loan = loanService.findById(id);
        return ResponseEntity.ok(loan);
    }

    // Create loan
    @PostMapping("/")
    public ResponseEntity<?> saveLoan(@RequestBody LoanEntity loan) {
        try {
            LoanEntity newloan = loanService.createLoan(loan);
            return ResponseEntity.ok(newloan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update loan
    @PutMapping("/")
    public ResponseEntity<LoanEntity> updateLoan(@RequestBody LoanEntity loan){
        LoanEntity loanUpdated = loanService.updateLoan(loan);
        return ResponseEntity.ok(loanUpdated);
    }

    // Get loans by rut
    @GetMapping("/by-rut/{rut}")
    public ResponseEntity<List<LoanEntity>> getLoansByRut(@PathVariable String rut){
        return ResponseEntity.ok(loanService.findByRutClient(rut));
    }

    // Delete loan by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoanByID(@PathVariable Long id) {
        try {
            var isDeleted = loanService.deleteLoan(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get tools by loan id
    @GetMapping("/tools-by-loan/{id}")
    public ResponseEntity<List<ToolEntity>> getToolsByLoanId(@PathVariable Long id){
        return ResponseEntity.ok(loanService.getToolsByLoanId(id));
    }

    // Update penalty loan
    @PutMapping("/update-penalty/{id}")
    public ResponseEntity<LoanEntity> updatePenaltyLoan(@PathVariable Long id){
        LoanEntity loanUpdated = loanService.updatePenaltyLoan(id);
        return ResponseEntity.ok(loanUpdated);
    }

    // Finish loan
    @PutMapping("/finish-loan/{id}/{totalValue}")
    public ResponseEntity<?> finishLoan(@PathVariable Long id, @PathVariable Integer totalValue) {
        try {
            return ResponseEntity.ok(loanService.finalizeLoan(id,totalValue));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get loans by range date
    @GetMapping("/loans-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<?> getLoansByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        try {
            return ResponseEntity.ok(loanService.getLoansByDateRange(initDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
