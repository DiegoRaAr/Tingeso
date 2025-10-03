package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.services.AdminService;
import com.example.tingeso1.services.KardexService;
import com.example.tingeso1.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tingeso1.entities.ToolEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
@CrossOrigin("*")
public class LoanController {
    @Autowired
    LoanService loanService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public ResponseEntity<List<LoanEntity>> listLoan(){
        List<LoanEntity> loans = loanService.getLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanEntity> getLoanById(@PathVariable Long id){
        LoanEntity loan = loanService.findById(id);
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/")
    public ResponseEntity<LoanEntity> saveLoan(@RequestBody LoanEntity loan) throws Exception {
        LoanEntity newloan = loanService.createLoan(loan);
        return ResponseEntity.ok(newloan);
    }

    @PutMapping("/")
    public ResponseEntity<LoanEntity> updateLoan(@RequestBody LoanEntity loan){
        LoanEntity loanUpdated = loanService.updateLoan(loan);
        return ResponseEntity.ok(loanUpdated);
    }

    @GetMapping("/by-rut/{rut}")
    public ResponseEntity<List<LoanEntity>> getLoansByRut(@PathVariable String rut){
        return ResponseEntity.ok(loanService.findByRutClient(rut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLoanByID(@PathVariable Long id) throws Exception{
        var isDeleted = loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tools-by-loan/{id}")
    public ResponseEntity<List<ToolEntity>> getToolsByLoanId(@PathVariable Long id){
        return ResponseEntity.ok(loanService.getToolsByLoanId(id));
    }

    @PutMapping("/update-penalty/{id}")
    public ResponseEntity<LoanEntity> updatePenaltyLoan(@PathVariable Long id){
        LoanEntity loanUpdated = loanService.updatePenaltyLoan(id);
        return ResponseEntity.ok(loanUpdated);
    }
}
