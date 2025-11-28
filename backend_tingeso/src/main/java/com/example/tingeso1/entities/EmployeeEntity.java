package com.example.tingeso1.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

// Entity Employee, this class represents the employee table in the database
@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idEmployee;

    private String rutEmployee;
    private String nameEmployee;
    private String stateEmployee;
    private String passwordEmployee;
}
