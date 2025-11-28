package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

// Entity Tool, this class represents the tool table in the database.
@Entity
@Table(name = "tool")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idTool;

    private String nameTool;
    private String categoryTool;
    private int totalValue;
    private String stateTool;
    private int stockTool;
    private int repairCharge;
    private int dailyCharge;
    private int lateCharge;

}
