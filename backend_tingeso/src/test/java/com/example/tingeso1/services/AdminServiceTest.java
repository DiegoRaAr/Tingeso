package com.example.tingeso1.services;

import com.example.tingeso1.entities.AdminEntity;
import com.example.tingeso1.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private AdminEntity admin1;
    private AdminEntity admin2;

    @BeforeEach
    void setup() {
        admin1 = new AdminEntity();
        admin1.setIdAdmin(1L);
        admin1.setNameAdmin("Juan Pérez");
        admin1.setRutAdmin("11-1");

        admin2 = new AdminEntity();
        admin2.setIdAdmin(2L);
        admin2.setNameAdmin("Ana Gómez");
        admin2.setRutAdmin("22-2");
    }

    // ---------- getAdmins ----------
    @Test
    void whenGetAdmins_thenReturnAllAdmins() {
        when(adminRepository.findAll()).thenReturn(new ArrayList<>(List.of(admin1, admin2)));

        ArrayList<AdminEntity> result = adminService.getAdmins();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameAdmin()).isEqualTo("Juan Pérez");
        verify(adminRepository, times(1)).findAll();
    }

    // ---------- saveAdmin ----------
    @Test
    void whenSaveAdmin_thenReturnSavedAdmin() {
        when(adminRepository.save(admin1)).thenReturn(admin1);

        AdminEntity result = adminService.saveAdmin(admin1);

        assertThat(result).isEqualTo(admin1);
        verify(adminRepository, times(1)).save(admin1);
    }

    // ---------- getAdminById ----------
    @Test
    void whenGetAdminById_thenReturnAdmin() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin1));

        AdminEntity result = adminService.getAdminById(1L);

        assertThat(result.getRutAdmin()).isEqualTo("11-1");
        verify(adminRepository, times(1)).findById(1L);
    }

    // ---------- getAdminByRut ----------
    @Test
    void whenGetAdminByRut_thenReturnAdmin() {
        when(adminRepository.findByRutAdmin("11-1")).thenReturn(admin1);

        AdminEntity result = adminService.getAdminByRut("11-1");

        assertThat(result.getNameAdmin()).isEqualTo("Juan Pérez");
        verify(adminRepository, times(1)).findByRutAdmin("11-1");
    }

    // ---------- updateAdmin ----------
    @Test
    void whenUpdateAdmin_thenReturnUpdatedAdmin() {
        admin1.setNameAdmin("Juan Actualizado");
        when(adminRepository.save(admin1)).thenReturn(admin1);

        AdminEntity result = adminService.updateAdmin(admin1);

        assertThat(result.getNameAdmin()).isEqualTo("Juan Actualizado");
        verify(adminRepository, times(1)).save(admin1);
    }

    // ---------- deleteAdmin ----------
    @Test
    void whenDeleteAdmin_thenReturnTrue() throws Exception {
        doNothing().when(adminRepository).deleteById(1L);

        boolean result = adminService.deleteAdmin(1L);

        assertThat(result).isTrue();
        verify(adminRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteAdminFails_thenThrowException() {
        doThrow(new RuntimeException("DB error")).when(adminRepository).deleteById(1L);

        Exception e = assertThrows(Exception.class, () -> adminService.deleteAdmin(1L));

        assertThat(e.getMessage()).contains("DB error");
        verify(adminRepository, times(1)).deleteById(1L);
    }
}
