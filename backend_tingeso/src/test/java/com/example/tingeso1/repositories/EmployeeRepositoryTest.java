package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.EmployeeEntity;
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
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntity e1;
    private EmployeeEntity e2;
    private EmployeeEntity e3;

    @BeforeEach
    void setup() {
        e1 = new EmployeeEntity();
        e1.setNameEmployee("Diego Ramos");
        e1.setRutEmployee("11-1");
        e1.setStateEmployee("ACTIVO");
        e1.setPasswordEmployee("1234");

        e2 = new EmployeeEntity();
        e2.setNameEmployee("Andrea Soto");
        e2.setRutEmployee("22-2");
        e2.setStateEmployee("INACTIVO");
        e2.setPasswordEmployee("abcd");

        e3 = new EmployeeEntity();
        e3.setNameEmployee("Brenda Núñez");
        e3.setRutEmployee("33-3");
        e3.setStateEmployee("ACTIVO");
        e3.setPasswordEmployee("5678");

        entityManager.persist(e1);
        entityManager.persist(e2);
        entityManager.persist(e3);
        entityManager.flush();
    }

    // ---------- findByRutEmployee ----------
    @Test
    void whenFindByRutEmployee_thenReturnMatchingEmployee() {
        EmployeeEntity found = employeeRepository.findByRutEmployee("11-1");

        assertThat(found).isNotNull();
        assertThat(found.getNameEmployee()).isEqualTo("Diego Ramos");
        assertThat(found.getStateEmployee()).isEqualTo("ACTIVO");
    }

    // ---------- findByNameEmployee ----------
    @Test
    void whenFindByNameEmployee_thenReturnCorrectEmployee() {
        EmployeeEntity found = employeeRepository.findByNameEmployee("Andrea Soto");

        assertThat(found).isNotNull();
        assertThat(found.getRutEmployee()).isEqualTo("22-2");
        assertThat(found.getPasswordEmployee()).isEqualTo("abcd");
    }

    // ---------- findByStateEmployee ----------
    @Test
    void whenFindByStateEmployee_thenReturnAllActiveEmployees() {
        List<EmployeeEntity> result = employeeRepository.findByStateEmployee("ACTIVO");

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nameEmployee").contains("Diego Ramos", "Brenda Núñez");
    }
}

