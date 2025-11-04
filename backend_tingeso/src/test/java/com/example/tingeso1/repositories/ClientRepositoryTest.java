package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.LoanEntity;
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
public class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private ClientEntity c1;
    private ClientEntity c2;
    private ClientEntity c3;

    @BeforeEach
    void setup() {
        c1 = new ClientEntity();
        c1.setRutClient("11-1");
        c1.setNameClient("Diego Ramos");
        c1.setStateClient("ACTIVO");
        c1.setEmailClient("diego@example.com");
        c1.setPhoneNumberClient("912345678");

        c2 = new ClientEntity();
        c2.setRutClient("22-2");
        c2.setNameClient("Andrea Soto");
        c2.setStateClient("RESTRINGIDO");
        c2.setEmailClient("andrea@example.com");
        c2.setPhoneNumberClient("976543210");

        c3 = new ClientEntity();
        c3.setRutClient("33-3");
        c3.setNameClient("Brenda Núñez");
        c3.setStateClient("ACTIVO");
        c3.setEmailClient("brenda@example.com");
        c3.setPhoneNumberClient("945678321");

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.persist(c3);
        entityManager.flush();
    }

    // ---------- findByRutClient ----------
    @Test
    void whenFindByRutClient_thenReturnCorrectClient() {
        ClientEntity found = clientRepository.findByRutClient("11-1");

        assertThat(found).isNotNull();
        assertThat(found.getNameClient()).isEqualTo("Diego Ramos");
        assertThat(found.getStateClient()).isEqualTo("ACTIVO");
    }

    // ---------- findByIdClient ----------
    @Test
    void whenFindByIdClient_thenReturnCorrectClient() {
        ClientEntity found = clientRepository.findByIdClient(c2.getIdClient());

        assertThat(found).isNotNull();
        assertThat(found.getNameClient()).isEqualTo("Andrea Soto");
        assertThat(found.getStateClient()).isEqualTo("RESTRINGIDO");
    }

    // ---------- findByNameClient ----------
    @Test
    void whenFindByNameClient_thenReturnMatchingClient() {
        ClientEntity found = clientRepository.findByNameClient("Brenda Núñez");

        assertThat(found).isNotNull();
        assertThat(found.getRutClient()).isEqualTo("33-3");
        assertThat(found.getEmailClient()).contains("brenda");
    }

    // ---------- findAByStateClient ----------
    @Test
    void whenFindAByStateClient_thenReturnClientWithMatchingState() {
        ClientEntity found = clientRepository.findAByStateClient("RESTRINGIDO");

        assertThat(found).isNotNull();
        assertThat(found.getNameClient()).isEqualTo("Andrea Soto");
        assertThat(found.getStateClient()).isEqualTo("RESTRINGIDO");
    }

    // ---------- findAllLoanByIdClient ----------
    @Test
    void whenFindAllLoanByIdClient_thenReturnLoansAssociated() {
        // given: prestamos asociados al cliente c1
        LoanEntity loan1 = new LoanEntity();
        loan1.setIdClient(c1);
        LoanEntity loan2 = new LoanEntity();
        loan2.setIdClient(c1);

        entityManager.persist(loan1);
        entityManager.persist(loan2);
        entityManager.flush();

        // when
        List<LoanEntity> loans = clientRepository.findAllLoanByIdClient(c1);

        // then
        assertThat(loans).hasSize(2);
        assertThat(loans.get(0).getIdClient().getRutClient()).isEqualTo("11-1");
    }
}
