package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "loan_tool",
            joinColumns = @JoinColumn(name = "idLoan"),
            inverseJoinColumns = @JoinColumn(name = "idTool")
    )
    private List<ToolEntity> tool;

    @ManyToOne
    @JoinColumn(name = "idEmployee", referencedColumnName = "idEmployee")
    private EmployeeEntity idEmployee;

}
