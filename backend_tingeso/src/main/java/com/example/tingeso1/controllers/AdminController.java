package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.AdminEntity;
import com.example.tingeso1.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //Rute for find all Admin
    @GetMapping("/")
    public ResponseEntity<List<AdminEntity>> listAdmins() {
        List<AdminEntity> admins = adminService.getAdmins();
        return ResponseEntity.ok(admins);
    }

    // Rute for find admin by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<AdminEntity>> getAdminByID(@PathVariable Long id) {
        Optional<AdminEntity> admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }

    // Create a Admin-
    @PostMapping("/")
    public ResponseEntity<AdminEntity> saveAdmin(@RequestBody AdminEntity adminEntity) {
        AdminEntity newAdmin = adminService.saveAdmin(adminEntity);
        return ResponseEntity.ok(newAdmin);
    }

    //Updaste admin
    @PutMapping("/")
    public ResponseEntity<AdminEntity> updateAdmin(@RequestBody AdminEntity admin) {
        AdminEntity adminUpdated = adminService.updateAdmin(admin);
        return ResponseEntity.ok(adminUpdated);
    }

    //Delete Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAdminById(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
