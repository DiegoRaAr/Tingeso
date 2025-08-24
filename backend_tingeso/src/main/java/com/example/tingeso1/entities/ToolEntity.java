package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@Entity
@Table(name = "tool")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_tool;

    private String name_tool;
    private String category_tool;
    private int total_value;
    private String state_tool;
    private int stock_tool;
    private int repair_charge;
    private int daily_charge;
    private int late_charge;
}
