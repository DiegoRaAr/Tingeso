package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.KardexEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class KardexRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KardexRepository kardexRepository;

    private KardexEntity kardex1;
    private KardexEntity kardex2;
    private KardexEntity kardex3;

    @BeforeEach
    void setup() {
        Calendar cal = Calendar.getInstance();

        cal.set(2024, Calendar.JANUARY, 5);
        Date d1 = cal.getTime();

        cal.set(2024, Calendar.FEBRUARY, 10);
        Date d2 = cal.getTime();

        cal.set(2024, Calendar.MARCH, 15);
        Date d3 = cal.getTime();

        kardex1 = new KardexEntity();
        kardex1.setNameTool("Martillo");
        kardex1.setStateTool("PRESTAMO");
        kardex1.setDateKardex(d1);

        kardex2 = new KardexEntity();
        kardex2.setNameTool("Taladro");
        kardex2.setStateTool("DEVOLUCIÓN");
        kardex2.setDateKardex(d2);

        kardex3 = new KardexEntity();
        kardex3.setNameTool("Sierra");
        kardex3.setStateTool("BAJA");
        kardex3.setDateKardex(d3);

        entityManager.persist(kardex1);
        entityManager.persist(kardex2);
        entityManager.persist(kardex3);
        entityManager.flush();
    }

    // ---------- findByIdKardex ----------
    @Test
    void whenFindByIdKardex_thenReturnCorrectKardex() {
        KardexEntity found = kardexRepository.findByIdKardex(kardex1.getIdKardex());

        assertThat(found).isNotNull();
        assertThat(found.getNameTool()).isEqualTo("Martillo");
        assertThat(found.getStateTool()).isEqualTo("PRESTAMO");
    }

    // ---------- findByDateKardexBetween ----------
    @Test
    void whenFindByDateKardexBetween_thenReturnMatchingRecords() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1);
        Date start = cal.getTime();
        cal.set(2024, Calendar.FEBRUARY, 28);
        Date end = cal.getTime();

        List<KardexEntity> result = kardexRepository.findByDateKardexBetween(start, end);

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nameTool").contains("Martillo", "Taladro");
    }
}
