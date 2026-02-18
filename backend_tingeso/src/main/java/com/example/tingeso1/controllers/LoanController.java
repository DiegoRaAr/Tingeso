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
    public ResponseEntity<LoanEntity> saveLoan(@RequestBody LoanEntity loan) throws Exception {
        LoanEntity newloan = loanService.createLoan(loan);
        return ResponseEntity.ok(newloan);
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
    public ResponseEntity<Boolean> deleteLoanByID(@PathVariable Long id) throws Exception{
        var isDeleted = loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<LoanEntity> finishLoan(@PathVariable Long id, @PathVariable Integer totalValue) throws Exception{
        return ResponseEntity.ok(loanService.finalizeLoan(id,totalValue));
    }

    // Get loans by range date
    @GetMapping("/loans-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<List<LoanEntity>> getLoansByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) throws Exception {
        return ResponseEntity.ok(loanService.getLoansByDateRange(initDate, endDate));
    }

    // Get num loans "RESTRINGIDO"
    @GetMapping("/num-loans-restringido/{rut}")
    public ResponseEntity<?> getNumLoansRestringido(@PathVariable String rut) {
        try {
            return ResponseEntity.ok(loanService.getNumLoanRestrinByRutClient(rut));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get num loans "ACTIVO"
    @GetMapping("/num-active-loans/{rut}")
    public ResponseEntity<?> getNumActiveLoans(@PathVariable String rut) {
        try {
            return ResponseEntity.ok(loanService.getNumActiveLoans(rut));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
