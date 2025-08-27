package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    public AdminEntity findByRutAdmin(String rut);
    public AdminEntity findByNameAdmin(String name);

    List<AdminEntity> findByStateAdmin(String state);
}
