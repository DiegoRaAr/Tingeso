package com.example.tingeso1.services;

import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.repositories.ClientRepository;
import com.example.tingeso1.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tingeso1.entities.ToolEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    // Find Loan
    public ArrayList<LoanEntity> getLoans(){
        return (ArrayList<LoanEntity>) loanRepository.findAll();
    }

    // Create Loan
    public LoanEntity createLoan(LoanEntity loanEntity){
        return loanRepository.save(loanEntity);
    }

    // Find Loan by Id
    public LoanEntity findById(Long id){
        return loanRepository.findById(id).get();
    }

    //Update Loan
    public LoanEntity updateLoan(LoanEntity loanEntity){
        return loanRepository.save(loanEntity);
    }

    // Delete Loam by id
    public boolean deleteLoan(Long id) throws Exception{
        try {
            loanRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // Get loan by rut client
    public List<LoanEntity> findByRutClient(String rut){
        return loanRepository.findByIdClient_RutClient(rut);
    }

    // Get tools by loan id
    public List<ToolEntity> getToolsByLoanId(Long id){
        LoanEntity loan = loanRepository.findByIdLoan(id);
        return loan.getTool();
    }
}
