package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Class AdminRepository, this class extends JpaRepository and is used to interact with the admin table in the database.
@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    // Function to find admin by rut.
    // Input: rut of admin(String)
    // Output: admin entity
    public AdminEntity findByRutAdmin(String rut);

    // Function to find admin by name.
    // Input: name of admin(String)
    // Output: admin entity
    public AdminEntity findByNameAdmin(String name);

    // Function to find admin by state.
    // Input: state of admin(String)
    // Output: list of admin entities
    List<AdminEntity> findByStateAdmin(String state);
}
