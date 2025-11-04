package com.example.tingeso1.services;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.repositories.KardexRepository;
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
class KardexServiceTest {

    @Mock
    private KardexRepository kardexRepository;

    @InjectMocks
    private KardexService kardexService;

    private KardexEntity kardex1;
    private KardexEntity kardex2;

    @BeforeEach
    void setup() {
        kardex1 = new KardexEntity();
        kardex1.setIdKardex(1L);
        kardex1.setNameTool("Martillo");
        kardex1.setStateTool("PRESTAMO");

        kardex2 = new KardexEntity();
        kardex2.setIdKardex(2L);
        kardex2.setNameTool("Serrucho");
        kardex2.setStateTool("DEVOLUCIÓN");
    }

    // ---------- getKardexes ----------
    @Test
    void whenGetKardexes_thenReturnAll() {
        when(kardexRepository.findAll()).thenReturn(new ArrayList<>(List.of(kardex1, kardex2)));


        ArrayList<KardexEntity> result = kardexService.getKardexes();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameTool()).isEqualTo("Martillo");
        verify(kardexRepository, times(1)).findAll();
    }

    // ---------- createKardex ----------
    @Test
    void whenCreateKardex_thenReturnSavedEntity() {
        when(kardexRepository.save(kardex1)).thenReturn(kardex1);

        KardexEntity result = kardexService.createKardex(kardex1);

        assertThat(result).isEqualTo(kardex1);
        verify(kardexRepository, times(1)).save(kardex1);
    }

    // ---------- getKardexById ----------
    @Test
    void whenGetKardexById_thenReturnEntity() {
        when(kardexRepository.findById(1L)).thenReturn(Optional.of(kardex1));

        KardexEntity result = kardexService.getKardexById(1L);

        assertThat(result.getNameTool()).isEqualTo("Martillo");
        verify(kardexRepository, times(1)).findById(1L);
    }

    // ---------- updateKardex ----------
    @Test
    void whenUpdateKardex_thenReturnUpdatedEntity() {
        when(kardexRepository.save(kardex2)).thenReturn(kardex2);

        KardexEntity result = kardexService.updateKardex(kardex2);

        assertThat(result).isEqualTo(kardex2);
        verify(kardexRepository, times(1)).save(kardex2);
    }

    // ---------- deleteKardex ----------
    @Test
    void whenDeleteKardex_thenReturnTrue() throws Exception {
        doNothing().when(kardexRepository).deleteById(1L);

        boolean result = kardexService.deleteKardex(1L);

        assertThat(result).isTrue();
        verify(kardexRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteKardexFails_thenThrowException() {
        doThrow(new RuntimeException("DB error")).when(kardexRepository).deleteById(1L);

        Exception e = assertThrows(Exception.class, () -> kardexService.deleteKardex(1L));

        assertThat(e.getMessage()).contains("DB error");
        verify(kardexRepository, times(1)).deleteById(1L);
    }
}
