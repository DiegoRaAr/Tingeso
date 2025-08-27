package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    // Find Employee
    public ArrayList<EmployeeEntity> getEmployees(){
        return (ArrayList<EmployeeEntity>)  employeeRepository.findAll();
    }

    // Create Employee
    public EmployeeEntity saveEmployee(EmployeeEntity employeeEntity){
        return employeeRepository.save(employeeEntity);
    }

    // Find Employee by Id
    public EmployeeEntity getEmployeeById(Long id){
        return employeeRepository.findById(id).get();
    }

    // Find Employee by rut
    public EmployeeEntity getEmployeeByRut(String rut){
        return  employeeRepository.findByRutEmployee(rut);
    }

    //Update Employee
    public EmployeeEntity updateEmployee(EmployeeEntity employee){
        return employeeRepository.save(employee);
    }

    // Delaete Employee
    public boolean deleteEmployee(Long id) throws Exception{
        try {
            employeeRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
