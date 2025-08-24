package com.example.tingeso1.services;

import com.example.tingeso1.entities.AdminEntity;
import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.repositories.AdminRepository;
import com.example.tingeso1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;

    // Find Andmin
    public ArrayList<AdminEntity> getAdmins(){
        return (ArrayList<AdminEntity>)  adminRepository.findAll();
    }

    // Create Admin
    public AdminEntity saveAdmin(AdminEntity adminEntity){
        return adminRepository.save(adminEntity);
    }

    // Find Admin by Id
    public AdminEntity getAdminById(Long id){
        return adminRepository.findById(id).get();
    }

    // find Admin by Rut
    public AdminEntity getAdminByRut(String rut){
        return adminRepository.findByRut_admin(rut);
    }

    // Update Admin
    public AdminEntity updateAdmin(AdminEntity admin){
        return adminRepository.save(admin);
    }

    // Delete a Admin
    public boolean deleteAdmin(Long id) throws Exception{
        try{
            adminRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
