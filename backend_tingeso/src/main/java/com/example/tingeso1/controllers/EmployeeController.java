package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@CrossOrigin("*")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/")
    public ResponseEntity<List<EmployeeEntity>> listEmployees() {
        List<EmployeeEntity> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getEmployeeByID(@PathVariable Long id) {
        EmployeeEntity employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/")
    public ResponseEntity<EmployeeEntity> saveEmployee(@RequestBody EmployeeEntity employeeEntity) {
        EmployeeEntity newEmployee = employeeService.saveEmployee(employeeEntity);
        return ResponseEntity.ok(newEmployee);
    }

    @PutMapping("/")
    public ResponseEntity<EmployeeEntity> updateEmployee(@RequestBody EmployeeEntity employeeEntity) {
        EmployeeEntity employeeUpdated = employeeService.updateEmployee(employeeEntity);
        return ResponseEntity.ok(employeeUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployeeByID(@PathVariable Long id) throws Exception {
        var isDeleted = employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
