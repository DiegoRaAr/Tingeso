package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.repositories.ClientRepository;
import com.example.tingeso1.repositories.KardexRepository;
import com.example.tingeso1.repositories.LoanRepository;
import com.example.tingeso1.repositories.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private KardexRepository kardexRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private LoanService loanService;

    private LoanEntity loan1;
    private LoanEntity loan2;
    private LoanEntity loan3;
    private LoanEntity loan;
    private ClientEntity client;
    private ToolEntity tool;

    private Date date(String yyyyMMdd) throws Exception {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(yyyyMMdd);
    }

    @BeforeEach
    void setup() throws Exception {
        loan1 = new LoanEntity();
        loan1.setIdLoan(1L);
        loan1.setInitDate(date("2024-01-10"));

        loan2 = new LoanEntity();
        loan2.setIdLoan(2L);
        loan2.setInitDate(date("2024-02-15"));

        loan3 = new LoanEntity();
        loan3.setIdLoan(3L);
        loan3.setInitDate(date("2024-03-20"));

        // Cliente activo inicialmente
        client = new ClientEntity();
        client.setIdClient(1L);
        client.setStateClient("ACTIVO");

        // Herramienta del préstamo
        tool = new ToolEntity();
        tool.setIdTool(1L);
        tool.setNameTool("Martillo");
        tool.setStockTool(2);
        tool.setStateTool("ACTIVA");
        tool.setLateCharge(1000);
        tool.setDailyCharge(1000);

        // Préstamo activo con esa herramienta
        loan = new LoanEntity();
        loan.setIdLoan(1L);
        loan.setIdClient(client);
        loan.setTool(List.of(tool));
        loan.setStateLoan("ACTIVO");
        loan.setPenaltyLoan(0);
        loan.setInitDate(date("2024-01-10"));
        loan.setEndDate(date("2024-01-15"));
    }

    // ---------- findById ----------
    @Test
    void whenFindById_thenReturnOptionalLoan() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan1));

        Optional<LoanEntity> result = loanService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getIdLoan()).isEqualTo(1L);
        verify(loanRepository, times(1)).findById(1L);
    }

    // ---------- updateLoan ----------
    @Test
    void whenUpdateLoan_thenReturnSavedLoan() {
        when(loanRepository.save(loan1)).thenReturn(loan1);

        LoanEntity result = loanService.updateLoan(loan1);

        assertThat(result).isEqualTo(loan1);
        verify(loanRepository, times(1)).save(loan1);
    }

    // ---------- deleteLoan ----------
    @Test
    void whenDeleteLoan_thenReturnTrue() throws Exception {
        doNothing().when(loanRepository).deleteById(1L);

        boolean result = loanService.deleteLoan(1L);

        assertThat(result).isTrue();
        verify(loanRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteLoanFails_thenThrowException() {
        doThrow(new RuntimeException("DB error")).when(loanRepository).deleteById(1L);

        Exception e = assertThrows(Exception.class, () -> loanService.deleteLoan(1L));

        assertThat(e.getMessage()).contains("DB error");
        verify(loanRepository, times(1)).deleteById(1L);
    }

    // ---------- findByRutClient ----------
    @Test
    void whenFindByRutClient_thenReturnLoansList() {
        when(loanRepository.findByIdClientRutClient("11-1")).thenReturn(List.of(loan1, loan2));

        List<LoanEntity> result = loanService.findByRutClient("11-1");

        assertThat(result).hasSize(2);
        verify(loanRepository, times(1)).findByIdClientRutClient("11-1");
    }

    // ---------- getToolsByLoanId ----------
    @Test
    void whenGetToolsByLoanId_thenReturnToolList() {
        when(loanRepository.findByIdLoan(1L)).thenReturn(Optional.of(loan));

        List<ToolEntity> result = loanService.getToolsByLoanId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNameTool()).isEqualTo("Martillo");
        verify(loanRepository, times(1)).findByIdLoan(1L);
    }

    @Test
    void whenGetToolsByLoanIdFails_thenReturnNull() {
        when(loanRepository.findByIdLoan(1L)).thenThrow(new RuntimeException("DB error"));

        Exception e = assertThrows(Exception.class, () -> loanService.getToolsByLoanId(1L));
        
        assertThat(e.getMessage()).contains("Error al obtener las herramientas del préstamo");
        verify(loanRepository, times(1)).findByIdLoan(1L);
    }

    // ---------- findLoansByRutClient ----------
    @Test
    void whenFindLoansByRutClient_thenReturnLoansList() {
        when(loanRepository.findByIdClientRutClient("11-1")).thenReturn(List.of(loan2, loan3));

        List<LoanEntity> result = loanService.findLoansByRutClient("11-1");

        assertThat(result).hasSize(2);
        verify(loanRepository, times(1)).findByIdClientRutClient("11-1");
    }

    @Test
    void whenValidRange_thenReturnLoansWithinRange() throws Exception {
        // Arrange
        Date start = date("2024-01-01");
        Date end = date("2024-02-28");

        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2, loan3));

        // Act
        List<LoanEntity> result = loanService.getLoansByDateRange(start, end);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("idLoan").containsExactly(1L, 2L);
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void whenNoLoansInRange_thenReturnEmptyList() throws Exception {
        // Arrange
        Date start = date("2024-04-01");
        Date end = date("2024-04-30");
        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2, loan3));

        // Act
        List<LoanEntity> result = loanService.getLoansByDateRange(start, end);

        // Assert
        assertThat(result).isEmpty();
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void whenStartDateIsNull_thenThrowException() {
        // Arrange
        Date end = new Date();

        // Act + Assert
        Exception e = assertThrows(Exception.class, () -> loanService.getLoansByDateRange(null, end));
        assertThat(e.getMessage()).isEqualTo("Las fechas no pueden ser nulas");
        verify(loanRepository, never()).findAll();
    }

    @Test
    void whenEndDateIsNull_thenThrowException() {
        // Arrange
        Date start = new Date();

        // Act + Assert
        Exception e = assertThrows(Exception.class, () -> loanService.getLoansByDateRange(start, null));
        assertThat(e.getMessage()).isEqualTo("Las fechas no pueden ser nulas");
        verify(loanRepository, never()).findAll();
    }

    @Test
    void whenEndDateBeforeStartDate_thenThrowException() throws Exception {
        // Arrange
        Date start = date("2024-05-01");
        Date end = date("2024-04-01");

        // Act + Assert
        Exception e = assertThrows(Exception.class, () -> loanService.getLoansByDateRange(start, end));
        assertThat(e.getMessage()).isEqualTo("La fecha de fin no puede ser anterior a la fecha de inicio");
        verify(loanRepository, never()).findAll();
    }

    @Test
    void whenLoanNotFound_thenThrowException() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        Exception e = assertThrows(Exception.class, () -> loanService.finalizeLoan(1L, 5000));
        assertThat(e.getMessage()).contains("Préstamo no encontrado");
        verify(loanRepository, never()).save(any());
    }

    @Test
    void whenToolStockBecomesOne_thenToolStateBecomesActiva() throws Exception {
        // Arrange
        tool.setStockTool(0); // simula que al sumar 1 pasa a 1 → estado debe cambiar a ACTIVA

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(kardexRepository.save(any(KardexEntity.class))).thenReturn(new KardexEntity());
        when(toolRepository.save(any(ToolEntity.class))).thenReturn(tool);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        LoanEntity result = loanService.finalizeLoan(1L, 9999);

        // Assert
        assertThat(result.getStateLoan()).isEqualTo("FINALIZADO");
        assertThat(tool.getStockTool()).isEqualTo(1);
        assertThat(tool.getStateTool()).isEqualTo("ACTIVA");
        verify(kardexRepository, atLeastOnce()).save(any(KardexEntity.class));
    }

    @Test
    void whenLoanIsFinalizado_thenReturnSameLoanWithoutChanges() {
        // Arrange
        loan.setStateLoan("FINALIZADO");
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Act
        LoanEntity result = loanService.updatePenaltyLoan(1L);

        // Assert
        assertThat(result).isEqualTo(loan);
        verify(loanRepository, never()).save(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void whenFinishDateIsBeforeToday_thenCalculatePenaltyAndRestrictClient() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -3); // hace 3 días
        loan.setEndDate(cal.getTime());

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        LoanEntity result = loanService.updatePenaltyLoan(1L);

        // Assert
        long expectedPenalty = 3 * 1000; // 3 días * 1000 de lateCharge
        assertThat(result.getPenaltyLoan()).isEqualTo(expectedPenalty);
        assertThat(client.getStateClient()).isEqualTo("RESTRINGIDO");
        verify(clientRepository, atLeastOnce()).save(client);
        verify(loanRepository, atLeastOnce()).save(loan);
    }

    @Test
    void whenFinishDateIsAfterToday_thenSetPenaltyToZero() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5); // vence en 5 días
        loan.setEndDate(cal.getTime());

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        LoanEntity result = loanService.updatePenaltyLoan(1L);

        // Assert
        assertThat(result.getPenaltyLoan()).isEqualTo(0);
        verify(clientRepository, never()).save(any());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void whenLoanNotFound_thenReturnNull() {
        // Arrange
        when(loanRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        // Act
        LoanEntity result = loanService.updatePenaltyLoan(1L);

        // Assert
        assertThat(result).isNull();
        verify(clientRepository, never()).save(any());
    }

    @Test
    void whenAllValid_thenCreateLoanSuccessfully() throws Exception {
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(List.of());
        when(toolRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(kardexRepository.save(any())).thenReturn(new KardexEntity());
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LoanEntity result = loanService.createLoan(loan);

        assertThat(result).isNotNull();
        assertThat(result.getTotalLoan()).isEqualTo(6000); // 1 tool * dailyCharge
        verify(toolRepository, atLeastOnce()).save(any());
        verify(kardexRepository, atLeastOnce()).save(any());
        verify(loanRepository, times(1)).save(loan);
    }

    // ---------- Herramienta no encontrada ----------
    @Test
    void whenToolNotFound_thenThrowException() {
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());

        Exception e = assertThrows(Exception.class, () -> loanService.createLoan(loan));
        assertThat(e.getMessage()).contains("Herramienta no encontrada");
    }

    // ---------- Cliente no encontrado ----------
    @Test
    void whenClientNotFound_thenThrowException() {
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        Exception e = assertThrows(Exception.class, () -> loanService.createLoan(loan));
        assertThat(e.getMessage()).contains("Cliente no encontrado");
    }

    // ---------- Cliente con 5 préstamos activos ----------
    @Test
    void whenClientHasFiveLoans_thenThrowException() {
        LoanEntity loanTest1 = new LoanEntity();
        loanTest1.setStateLoan("ACTIVO");
        LoanEntity loanTest2 = new LoanEntity();
        loanTest2.setStateLoan("ACTIVO");
        LoanEntity loanTest3 = new LoanEntity();
        loanTest3.setStateLoan("ACTIVO");
        LoanEntity loanTest4 = new LoanEntity();
        loanTest4.setStateLoan("ACTIVO");
        LoanEntity loanTest5 = new LoanEntity();
        loanTest5.setStateLoan("ACTIVO");
        
        List<LoanEntity> loans = Arrays.asList(loanTest1, loanTest2, loanTest3, loanTest4, loanTest5);
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(loans);

        Exception e = assertThrows(Exception.class, () -> loanService.createLoan(loan));
        assertThat(e.getMessage()).contains("5 prestamos activos");
    }

    // ---------- Cliente con deuda pendiente ----------
    @Test
    void whenClientHasDebt_thenThrowException() {
        LoanEntity debtLoan = new LoanEntity();
        debtLoan.setPenaltyLoan(200);
        debtLoan.setStateLoan("ACTIVO");
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(List.of(debtLoan));

        Exception e = assertThrows(Exception.class, () -> loanService.createLoan(loan));
        assertThat(e.getMessage()).contains("deudas pendientes");
    }

    // ---------- Cliente restringido ----------
    @Test
    void whenClientRestricted_thenThrowException() {
        client.setStateClient("RESTRINGIDO");
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(List.of());

        Exception e = assertThrows(Exception.class, () -> loanService.createLoan(loan));
        assertThat(e.getMessage()).contains("restringido");
    }

    // ---------- Fecha fin antes de inicio ----------
    @Test
    void whenEndDateBeforeInitDate_thenThrowException() throws Exception {
        // Usamos el helper date(String) que ya existe en tu clase
        loan.setEndDate(date("2024-01-05")); // antes que initDate

        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(List.of());

        Exception e = assertThrows(Exception.class, () -> loanService.createLoan(loan));
        assertThat(e.getMessage()).contains("anterior a la fecha de inicio");
    }

    // ---------- Herramienta queda en stock 0 y cambia a BAJA ----------
    @Test
    void whenToolStockBecomesZero_thenSetStateBaja() throws Exception {
        tool.setStockTool(1); // quedará 0 después del préstamo
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(List.of());
        when(toolRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(kardexRepository.save(any())).thenReturn(new KardexEntity());
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LoanEntity result = loanService.createLoan(loan);

        assertThat(result).isNotNull();
        assertThat(tool.getStockTool()).isEqualTo(0);
        assertThat(tool.getStateTool()).isEqualTo("BAJA");
        verify(kardexRepository, atLeastOnce()).save(any());
    }

    @Test
    void whenGetLoans_thenReturnAllLoans() {
        // Arrange
        when(loanRepository.findAll()).thenReturn(new ArrayList<>(List.of(loan1, loan2, loan3)));

        // Act
        ArrayList<LoanEntity> result = loanService.getLoans();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getIdLoan()).isEqualTo(1L);
        assertThat(result.get(1).getIdLoan()).isEqualTo(2L);
        assertThat(result.get(2).getIdLoan()).isEqualTo(3L);
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void whenClientRestrictedAndNoDebt_thenSetClientToActivo() throws Exception {
        // Arrange
        client.setStateClient("RESTRINGIDO");
        LoanEntity debtFreeLoan = new LoanEntity();
        debtFreeLoan.setPenaltyLoan(0);
        debtFreeLoan.setStateLoan("FINALIZADO");

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(kardexRepository.save(any())).thenReturn(new KardexEntity());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(clientRepository.findAllLoanByIdClient(client)).thenReturn(List.of(debtFreeLoan));

        // Act
        LoanEntity result = loanService.finalizeLoan(1L, 9999);

        // Assert
        assertThat(result.getStateLoan()).isEqualTo("FINALIZADO");
        assertThat(client.getStateClient()).isEqualTo("ACTIVO");
        verify(clientRepository, atLeastOnce()).save(client);
    }

}


