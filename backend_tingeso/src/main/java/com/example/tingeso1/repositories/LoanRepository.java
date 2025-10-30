package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    public Optional<LoanEntity>  findByIdLoan(Long loanId);

    List<LoanEntity> findByInitDateBetween(Date start, Date end);
    List<LoanEntity> findByStateLoan(String state);

    @Query("SELECT l FROM LoanEntity l JOIN l.idClient c WHERE c.rutClient = :rut")
    List<LoanEntity> findByIdClient_RutClient(@Param("rut") String rut);
}
