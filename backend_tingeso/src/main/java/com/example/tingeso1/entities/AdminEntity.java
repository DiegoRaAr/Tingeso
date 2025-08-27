package com.example.tingeso1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.format.annotation.DateTimeFormat;

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
}
