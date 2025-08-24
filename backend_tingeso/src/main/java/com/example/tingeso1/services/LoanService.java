package com.example.tingeso1.services;

import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
}
