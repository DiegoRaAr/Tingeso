package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    public EmployeeEntity findByRutEmployee(String rut);
    public EmployeeEntity findByNameEmployee(String name);

    List<EmployeeEntity> findByStateEmployee(String state);
}
