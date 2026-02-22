package com.example.tingeso1.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Entity Admin, this class represents the admin table in the database
@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idAdmin;

    private String rutAdmin;
    private String nameAdmin;
    private String stateAdmin;
    private String passwordAdmin;

    @ManyToMany
    @JoinTable(
            name = "admin_loan",
            joinColumns = @JoinColumn(name = "idAdmin"),
            inverseJoinColumns = @JoinColumn(name = "idLoan")
    )
    private List<LoanEntity> loan;

    @ManyToMany
    @JoinTable(
            name = "admin_client",
            joinColumns = @JoinColumn(name = "idAdmin"),
            inverseJoinColumns = @JoinColumn(name = "idClient")
    )
    private List<ClientEntity> client;

    @ManyToMany
    @JoinTable(
            name = "admin_tool",
            joinColumns = @JoinColumn(name = "idAdmin"),
            inverseJoinColumns = @JoinColumn(name = "idTool")
    )
    private List<ToolEntity> tool;

    @ManyToMany
    @JoinTable(
            name = "admin_employee",
            joinColumns = @JoinColumn(name = "idAdmin"),
            inverseJoinColumns = @JoinColumn(name = "idEmployee")
    )
    private List<EmployeeEntity> employee;
}
