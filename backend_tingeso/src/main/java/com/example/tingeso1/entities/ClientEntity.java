package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idClient;

    private String rutClient;
    private String nameClient;
    private String stateClient;
    private String emailClient;
    private String phoneNumberClient;

    @ManyToOne
    @JoinColumn(name = "idLoan", referencedColumnName = "idLoan")
    private LoanEntity idloan;
}
