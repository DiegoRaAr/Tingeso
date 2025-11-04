package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.AdminEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    private AdminEntity a1;
    private AdminEntity a2;
    private AdminEntity a3;

    @BeforeEach
    void setup() {
        a1 = new AdminEntity();
        a1.setRutAdmin("11-1");
        a1.setNameAdmin("Diego Ramos");
        a1.setStateAdmin("ACTIVO");
        a1.setPasswordAdmin("1234");

        a2 = new AdminEntity();
        a2.setRutAdmin("22-2");
        a2.setNameAdmin("Andrea Soto");
        a2.setStateAdmin("INACTIVO");
        a2.setPasswordAdmin("abcd");

        a3 = new AdminEntity();
        a3.setRutAdmin("33-3");
        a3.setNameAdmin("Brenda Núñez");
        a3.setStateAdmin("ACTIVO");
        a3.setPasswordAdmin("5678");

        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.persist(a3);
        entityManager.flush();
    }

    // ---------- findByRutAdmin ----------
    @Test
    void whenFindByRutAdmin_thenReturnCorrectAdmin() {
        AdminEntity found = adminRepository.findByRutAdmin("11-1");

        assertThat(found).isNotNull();
        assertThat(found.getNameAdmin()).isEqualTo("Diego Ramos");
        assertThat(found.getStateAdmin()).isEqualTo("ACTIVO");
    }

    // ---------- findByNameAdmin ----------
    @Test
    void whenFindByNameAdmin_thenReturnCorrectAdmin() {
        AdminEntity found = adminRepository.findByNameAdmin("Andrea Soto");

        assertThat(found).isNotNull();
        assertThat(found.getRutAdmin()).isEqualTo("22-2");
        assertThat(found.getStateAdmin()).isEqualTo("INACTIVO");
    }

    // ---------- findByStateAdmin ----------
    @Test
    void whenFindByStateAdmin_thenReturnAllActiveAdmins() {
        List<AdminEntity> result = adminRepository.findByStateAdmin("ACTIVO");

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nameAdmin").contains("Diego Ramos", "Brenda Núñez");
    }
}
