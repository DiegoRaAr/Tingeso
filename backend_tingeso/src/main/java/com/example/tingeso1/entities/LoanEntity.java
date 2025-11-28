package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

// Entity Loan, this class represents the loan table in the database.
// Loan is a table that stores the loans of the tools for the clients.
@Entity
@Table(name = "loan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idLoan;

    private Date initDate;
    private LocalTime hourLoan;
    private Date endDate;
    private String stateLoan;
    private int penaltyLoan;
    private int totalLoan;

    @ManyToMany
    @JoinTable(
            name = "loan_tool",
            joinColumns = @JoinColumn(name = "idLoan"),
            inverseJoinColumns = @JoinColumn(name = "idTool")
    )
    private List<ToolEntity> tool;

    @ManyToOne
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    private ClientEntity idClient;

    public ClientEntity findAllLoanByIdClient(Long idClient2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllLoanByIdClient'");
    }

}
