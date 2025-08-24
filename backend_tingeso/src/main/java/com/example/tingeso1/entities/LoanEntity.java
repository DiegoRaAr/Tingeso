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
    private Long id_loan;

    private Date init_date;
    private LocalTime hour_loan;
    private Date end_date;
    private String state_loan;
    private int penalty_loan;

}
