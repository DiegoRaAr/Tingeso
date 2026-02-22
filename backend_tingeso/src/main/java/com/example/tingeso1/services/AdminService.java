package com.example.tingeso1.services;
import com.example.tingeso1.entities.AdminEntity;
import com.example.tingeso1.exceptions.DataPersistenceException;
import com.example.tingeso1.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Find Andmin
    public ArrayList<AdminEntity> getAdmins() {
        return (ArrayList<AdminEntity>) adminRepository.findAll();
    }

    // Create Admin
    public AdminEntity saveAdmin(AdminEntity adminEntity) {
        return adminRepository.save(adminEntity);
    }

    // Find Admin by Id
    public Optional<AdminEntity> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    // find Admin by Rut
    public AdminEntity getAdminByRut(String rut) {
        return adminRepository.findByRutAdmin(rut);
    }

    // Update Admin
    public AdminEntity updateAdmin(AdminEntity admin) {
        return adminRepository.save(admin);
    }

    // Delete a Admin
    public boolean deleteAdmin(Long id) {
        try {
            adminRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DataPersistenceException("Error deleting admin with id: " + id, e);
        }
    }
}
