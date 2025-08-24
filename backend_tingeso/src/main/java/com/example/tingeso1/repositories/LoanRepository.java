package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    public LoanEntity  findById_loan(Long loanId);

    List<LoanEntity> findByInit_dateBetween(Date start, Date end);
    List<LoanEntity> findByState_loan(String state);
}
