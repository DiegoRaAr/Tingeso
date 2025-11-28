package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

// Entity Kardex, this class represents the kardex table in the database.
// KArdex is a table that stores the changes of the tools.
@Entity
@Table(name = "kardex")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KardexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idKardex;

    private Date dateKardex;
    private String stateTool;
    private Long idTool;
    private String nameTool;
}
