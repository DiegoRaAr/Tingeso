package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

     @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get all employees
    @GetMapping("/")
    public ResponseEntity<List<EmployeeEntity>> listEmployees() {
        List<EmployeeEntity> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

    // Get employee by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<EmployeeEntity>> getEmployeeByID(@PathVariable Long id) {
        Optional<EmployeeEntity> employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // Create employee
    @PostMapping("/")
    public ResponseEntity<EmployeeEntity> saveEmployee(@RequestBody EmployeeEntity employeeEntity) {
        EmployeeEntity newEmployee = employeeService.saveEmployee(employeeEntity);
        return ResponseEntity.ok(newEmployee);
    }

    // Update employee
    @PutMapping("/")
    public ResponseEntity<EmployeeEntity> updateEmployee(@RequestBody EmployeeEntity employeeEntity) {
        EmployeeEntity employeeUpdated = employeeService.updateEmployee(employeeEntity);
        return ResponseEntity.ok(employeeUpdated);
    }

    // Delete employee by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployeeByID(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
