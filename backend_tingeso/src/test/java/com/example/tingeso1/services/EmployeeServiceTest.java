package com.example.tingeso1.services;

import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.repositories.EmployeeRepository;
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
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeEntity emp1;
    private EmployeeEntity emp2;

    @BeforeEach
    void setup() {
        emp1 = new EmployeeEntity();
        emp1.setIdEmployee(1L);
        emp1.setNameEmployee("Juan Pérez");
        emp1.setRutEmployee("11-1");

        emp2 = new EmployeeEntity();
        emp2.setIdEmployee(2L);
        emp2.setNameEmployee("Ana Gómez");
        emp2.setRutEmployee("22-2");
    }

    // ---------- getEmployees ----------
    @Test
    void whenGetEmployees_thenReturnAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>(List.of(emp1, emp2)));

        ArrayList<EmployeeEntity> result = employeeService.getEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameEmployee()).isEqualTo("Juan Pérez");
        verify(employeeRepository, times(1)).findAll();
    }

    // ---------- saveEmployee ----------
    @Test
    void whenSaveEmployee_thenReturnSavedEmployee() {
        when(employeeRepository.save(emp1)).thenReturn(emp1);

        EmployeeEntity result = employeeService.saveEmployee(emp1);

        assertThat(result).isEqualTo(emp1);
        verify(employeeRepository, times(1)).save(emp1);
    }

    // ---------- getEmployeeById ----------
    @Test
    void whenGetEmployeeById_thenReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp1));

        Optional<EmployeeEntity> result = employeeService.getEmployeeById(1L);

        assertThat(result.get().getRutEmployee()).isEqualTo("11-1");
        verify(employeeRepository, times(1)).findById(1L);
    }

    // ---------- getEmployeeByRut ----------
    @Test
    void whenGetEmployeeByRut_thenReturnEmployee() {
        when(employeeRepository.findByRutEmployee("11-1")).thenReturn(emp1);

        EmployeeEntity result = employeeService.getEmployeeByRut("11-1");

        assertThat(result.getNameEmployee()).isEqualTo("Juan Pérez");
        verify(employeeRepository, times(1)).findByRutEmployee("11-1");
    }

    // ---------- updateEmployee ----------
    @Test
    void whenUpdateEmployee_thenReturnUpdatedEmployee() {
        emp1.setNameEmployee("Juan Actualizado");
        when(employeeRepository.save(emp1)).thenReturn(emp1);

        EmployeeEntity result = employeeService.updateEmployee(emp1);

        assertThat(result.getNameEmployee()).isEqualTo("Juan Actualizado");
        verify(employeeRepository, times(1)).save(emp1);
    }

    // ---------- deleteEmployee ----------
    @Test
    void whenDeleteEmployee_thenReturnTrue() throws Exception {
        doNothing().when(employeeRepository).deleteById(1L);

        boolean result = employeeService.deleteEmployee(1L);

        assertThat(result).isTrue();
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteEmployeeFails_thenThrowException() {
        doThrow(new RuntimeException("DB error")).when(employeeRepository).deleteById(1L);

        Exception e = assertThrows(Exception.class, () -> employeeService.deleteEmployee(1L));

        assertThat(e.getMessage()).contains("Error deleting employee with id: 1");
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
