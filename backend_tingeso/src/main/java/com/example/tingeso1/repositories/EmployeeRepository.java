package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Class EmployeeRepository, this class extends JpaRepository
// and is used to interact with the employee table in the database.
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    // Function to find employee by rut.
    // Input: rut of employee(String)
    // Output: employee entity
    public EmployeeEntity findByRutEmployee(String rut);

    // Function to find employee by name.
    // Input: name of employee(String)
    // Output: employee entity
    public EmployeeEntity findByNameEmployee(String name);

    // Function to find employee by state.
    // Input: state of employee(String)
    // Output: list of employee entities
    List<EmployeeEntity> findByStateEmployee(String state);
}
