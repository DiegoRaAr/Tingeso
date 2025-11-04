package com.example.tingeso1.services;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.repositories.KardexRepository;
import com.example.tingeso1.repositories.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToolServiceTest {

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private KardexRepository kardexRepository;

    @Spy
    @InjectMocks
    private ToolService toolService;

    private ToolEntity tool;

    @BeforeEach
    void setup() {
        tool = new ToolEntity();
        tool.setIdTool(1L);
        tool.setNameTool("Martillo");
        tool.setCategoryTool("Manual");
        tool.setTotalValue(10000);
        tool.setStateTool("ACTIVO");
        tool.setStockTool(2);
        tool.setRepairCharge(5000);
        tool.setDailyCharge(2000);
        tool.setLateCharge(3000);
    }

    @Test
    void whenStockGreaterThanOne_thenSubtractAndSaveWithDisminucion() throws Exception {
        doReturn(tool).when(toolService).findById(1L);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);

        boolean result = toolService.subtractTool(1L);

        assertThat(result).isTrue();
        assertThat(tool.getStockTool()).isEqualTo(1);
        assertThat(tool.getStateTool()).isEqualTo("ACTIVO");
        verify(kardexRepository, times(1)).save(any(KardexEntity.class));
        verify(toolRepository, times(1)).save(any(ToolEntity.class));
    }

    @Test
    void whenStockEqualsOne_thenSetStateBajaAndSave() throws Exception {
        tool.setStockTool(1);
        doReturn(tool).when(toolService).findById(1L);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);

        boolean result = toolService.subtractTool(1L);

        assertThat(result).isTrue();
        assertThat(tool.getStockTool()).isEqualTo(0);
        assertThat(tool.getStateTool()).isEqualTo("BAJA");
        verify(kardexRepository, times(1)).save(any(KardexEntity.class));
        verify(toolRepository, times(1)).save(any(ToolEntity.class));
    }

    @Test
    void whenStockZero_thenThrowException() {
        tool.setStockTool(0);
        doReturn(tool).when(toolService).findById(1L);

        assertThrows(Exception.class, () -> toolService.subtractTool(1L));
    }

    @Test
    void whenAddTool_thenIncreaseStockAndSaveWithSuma() {
        // Arrange
        doReturn(tool).when(toolService).findById(1L);
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);

        // Act
        boolean result = toolService.addTool(1L);

        // Assert
        assertThat(result).isTrue();
        assertThat(tool.getStockTool()).isEqualTo(3); // stock +1
        verify(kardexRepository, times(1)).save(any(KardexEntity.class));
        verify(toolRepository, times(1)).save(any(ToolEntity.class));
    }

    @Test
    void whenGetBestToolsByRangeDate_thenReturnToolsOrderedByPrestamos() {
        // Arrange
        java.util.Date initDate = new java.util.Date(System.currentTimeMillis() - 86400000L); // ayer
        java.util.Date endDate = new java.util.Date(); // hoy

        // Creamos Kardex de distintos tipos
        KardexEntity k1 = new KardexEntity(); // Martillo prestado 2 veces
        k1.setIdTool(1L);
        k1.setStateTool("PRESTAMO");
        k1.setDateKardex(new java.util.Date());

        KardexEntity k2 = new KardexEntity();
        k2.setIdTool(1L);
        k2.setStateTool("PRESTAMO");
        k2.setDateKardex(new java.util.Date());

        KardexEntity k3 = new KardexEntity(); // Taladro prestado 1 vez
        k3.setIdTool(2L);
        k3.setStateTool("PRESTAMO");
        k3.setDateKardex(new java.util.Date());

        KardexEntity k4 = new KardexEntity(); // registro no relacionado
        k4.setIdTool(3L);
        k4.setStateTool("SUMA");
        k4.setDateKardex(new java.util.Date());

        // Mock del repositorio Kardex
        when(kardexRepository.findByDateKardexBetween(initDate, endDate))
                .thenReturn(List.of(k1, k2, k3, k4));

        // Mock del repositorio Tool (para devolver ToolEntity correspondientes)
        ToolEntity martillo = new ToolEntity();
        martillo.setIdTool(1L);
        martillo.setNameTool("Martillo");

        ToolEntity taladro = new ToolEntity();
        taladro.setIdTool(2L);
        taladro.setNameTool("Taladro");

        when(toolRepository.findById(1L)).thenReturn(java.util.Optional.of(martillo));
        when(toolRepository.findById(2L)).thenReturn(java.util.Optional.of(taladro));

        // Act
        List<ToolEntity> result = toolService.getBestToolsByRangeDate(initDate, endDate);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameTool()).isEqualTo("Martillo"); // más prestado
        assertThat(result.get(1).getNameTool()).isEqualTo("Taladro");  // segundo
        verify(kardexRepository, times(1)).findByDateKardexBetween(initDate, endDate);
        verify(toolRepository, times(2)).findById(anyLong());
    }

    @Test
    void whenNoPrestamosInRange_thenReturnEmptyList() {
        // Arrange
        java.util.Date initDate = new java.util.Date(System.currentTimeMillis() - 86400000L);
        java.util.Date endDate = new java.util.Date();

        // Sin registros PRESTAMO
        when(kardexRepository.findByDateKardexBetween(initDate, endDate))
                .thenReturn(List.of());

        // Act
        List<ToolEntity> result = toolService.getBestToolsByRangeDate(initDate, endDate);

        // Assert
        assertThat(result).isEmpty();
        verify(kardexRepository, times(1)).findByDateKardexBetween(initDate, endDate);
        verify(toolRepository, never()).findById(anyLong());
    }

    @Test
    void whenGetTools_thenReturnAllTools() {
        // Arrange
        ToolEntity t1 = new ToolEntity();
        t1.setIdTool(1L);
        t1.setNameTool("Martillo");
        ToolEntity t2 = new ToolEntity();
        t2.setIdTool(2L);
        t2.setNameTool("Taladro");

        when(toolRepository.findAll()).thenReturn(new ArrayList<>(List.of(t1, t2)));

        // Act
        ArrayList<ToolEntity> result = toolService.getTools();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameTool()).isEqualTo("Martillo");
        verify(toolRepository, times(1)).findAll();
    }

    @Test
    void whenCreateTool_thenSaveAndReturnTool() {
        // Arrange
        when(toolRepository.save(tool)).thenReturn(tool);

        // Act
        ToolEntity result = toolService.createTool(tool);

        // Assert
        assertThat(result).isEqualTo(tool);
        verify(toolRepository, times(1)).save(tool);
    }

    @Test
    void whenFindById_thenReturnTool() {
        // Arrange
        when(toolRepository.findById(1L)).thenReturn(java.util.Optional.of(tool));

        // Act
        ToolEntity result = toolService.findById(1L);

        // Assert
        assertThat(result).isEqualTo(tool);
        verify(toolRepository, times(1)).findById(1L);
    }

    @Test
    void whenUpdateTool_thenSaveAndReturnUpdatedTool() {
        // Arrange
        tool.setNameTool("Martillo reforzado");
        when(toolRepository.save(tool)).thenReturn(tool);

        // Act
        ToolEntity result = toolService.updateTool(tool);

        // Assert
        assertThat(result.getNameTool()).isEqualTo("Martillo reforzado");
        verify(toolRepository, times(1)).save(tool);
    }

    @Test
    void whenDeleteTool_thenReturnTrue() throws Exception {
        // Arrange
        doNothing().when(toolRepository).deleteById(1L);

        // Act
        boolean result = toolService.deleteTool(1L);

        // Assert
        assertThat(result).isTrue();
        verify(toolRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteToolThrowsException_thenPropagateException() {
        // Arrange
        doThrow(new RuntimeException("Error")).when(toolRepository).deleteById(1L);

        // Act + Assert
        assertThrows(Exception.class, () -> toolService.deleteTool(1L));
        verify(toolRepository, times(1)).deleteById(1L);
    }

}

