package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name = "idTool", referencedColumnName = "idTool")
    private ToolEntity idTool;

    @ManyToOne
    @JoinColumn(name = "idEmployee", referencedColumnName = "idEmployee")
    private EmployeeEntity idEmployee;

    @ManyToOne
    @JoinColumn(name = "idKardex", referencedColumnName = "idKardex")
    private KardexEntity idKardex;
}
