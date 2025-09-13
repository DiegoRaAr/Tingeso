package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

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
