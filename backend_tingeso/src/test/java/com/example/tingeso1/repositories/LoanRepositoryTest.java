package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanRepository loanRepository;

    private ClientEntity client1;
    private ClientEntity client2;
    private LoanEntity loan1;
    private LoanEntity loan2;
    private LoanEntity loan3;

    @BeforeEach
    void setup() {
        client1 = new ClientEntity();
        client1.setRutClient("11-1");
        client1.setStateClient("ACTIVO");

        client2 = new ClientEntity();
        client2.setRutClient("22-2");
        client2.setStateClient("RESTRINGIDO");

        entityManager.persist(client1);
        entityManager.persist(client2);

        Calendar cal = Calendar.getInstance();

        cal.set(2024, Calendar.JANUARY, 10);
        Date date1 = cal.getTime();

        cal.set(2024, Calendar.FEBRUARY, 5);
        Date date2 = cal.getTime();

        cal.set(2024, Calendar.MARCH, 15);
        Date date3 = cal.getTime();

        loan1 = new LoanEntity();
        loan1.setIdClient(client1);
        loan1.setInitDate(date1);
        loan1.setStateLoan("ACTIVO");

        loan2 = new LoanEntity();
        loan2.setIdClient(client1);
        loan2.setInitDate(date2);
        loan2.setStateLoan("FINALIZADO");

        loan3 = new LoanEntity();
        loan3.setIdClient(client2);
        loan3.setInitDate(date3);
        loan3.setStateLoan("ACTIVO");

        entityManager.persist(loan1);
        entityManager.persist(loan2);
        entityManager.persist(loan3);
        entityManager.flush();
    }

    // ---------- findByIdLoan ----------
    @Test
    void whenFindByIdLoan_thenReturnLoan() {
        Optional<LoanEntity> found = loanRepository.findByIdLoan(loan1.getIdLoan());
        assertThat(found).isPresent();
        assertThat(found.get().getIdClient().getRutClient()).isEqualTo("11-1");
    }

    // ---------- findByInitDateBetween ----------
    @Test
    void whenFindByInitDateBetween_thenReturnLoansWithinRange() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1);
        Date start = cal.getTime();
        cal.set(2024, Calendar.FEBRUARY, 28);
        Date end = cal.getTime();

        List<LoanEntity> result = loanRepository.findByInitDateBetween(start, end);

        assertThat(result).hasSize(2);
        assertThat(result).extracting("stateLoan").contains("ACTIVO", "FINALIZADO");
    }

    // ---------- findByStateLoan ----------
    @Test
    void whenFindByStateLoan_thenReturnActiveLoans() {
        List<LoanEntity> result = loanRepository.findByStateLoan("ACTIVO");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStateLoan()).isEqualTo("ACTIVO");
    }

    // ---------- findByIdClient_RutClient ----------
    @Test
    void whenFindByIdClientRutClient_thenReturnClientLoans() {
        List<LoanEntity> result = loanRepository.findByIdClient_RutClient("11-1");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIdClient().getRutClient()).isEqualTo("11-1");
    }
}
