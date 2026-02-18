package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.entities.LoanEntity;
import com.example.tingeso1.repositories.ClientRepository;
import com.example.tingeso1.repositories.KardexRepository;
import com.example.tingeso1.repositories.LoanRepository;
import com.example.tingeso1.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tingeso1.entities.ToolEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ToolRepository toolRepository;


    // Find Loan
    public ArrayList<LoanEntity> getLoans(){
        return (ArrayList<LoanEntity>) loanRepository.findAll();
    }

    // Create Loan with conditions
    public LoanEntity createLoan(LoanEntity loan) throws Exception {
        // Search all tools of loan, and verify if they exist
        List<ToolEntity> completeTools = new ArrayList<>();
        for (ToolEntity t : loan.getTool()) {
            ToolEntity tool = toolRepository.findById(t.getIdTool()).orElseThrow(() -> new Exception("Herramienta no encontrada"));
            completeTools.add(tool);
        }

        // Search client of loan, and verify if it exists
        ClientEntity client = clientRepository.findById(loan.getIdClient().getIdClient())
        .orElseThrow(() -> new Exception("Cliente no encontrado"));

        // Get all loans of client and verify if it has debt
        boolean debt = false;
        int activeLoans = 0;
        List<LoanEntity> loans = clientRepository.findAllLoanByIdClient(client);
        for (LoanEntity l: loans) {
            int penalty = l.getPenaltyLoan();
            if (penalty > 0 && l.getStateLoan().equals("ACTIVO")) {
                debt = true;
            }
            if (l.getStateLoan().equals("ACTIVO")) {
                activeLoans++;
            }
        }

        // Verify if client has 5 active loans
        if (activeLoans >= 5) {
            throw new Exception("El cliente ya tiene 5 prestamos activos");
        }

        // Verify if client has debt
        if (debt) {
            throw new Exception("El cliente tiene deudas pendientes");
        }

        // Verify if client is restricted
        if (client.getStateClient().equals("RESTRINGIDO")) {
            throw new Exception("El cliente se encuentra restringido");
        }

        // Verify if end date is before start date
        Date iniDate = loan.getInitDate();
        Date endDate = loan.getEndDate();

        if (endDate.before(iniDate)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        LocalDate start = iniDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculate days of loans
        long days = ChronoUnit.DAYS.between(start, end) + 1;

        // Calculate total of loans and update stock of tools
        int total = 0;
        for (ToolEntity tool: completeTools) {
            tool.setStockTool(tool.getStockTool() - 1);
            toolRepository.save(tool);

            if (tool.getStockTool() == 0) {
                tool.setStateTool("BAJA");
            }

            total += tool.getDailyCharge();

            // Create kardex for each tool
            KardexEntity kardex = new KardexEntity();
            kardex.setDateKardex(new java.util.Date());
            kardex.setIdTool(tool.getIdTool());
            kardex.setNameTool(tool.getNameTool());

            kardex.setStateTool("PRESTAMO");

            kardexRepository.save(kardex);
        }

        // Calculate total of loans
        total = Math.toIntExact(total * days);

        // Set variables of loan
        loan.setHourLoan(LocalTime.now());
        loan.setTool(completeTools);
        loan.setIdClient(client);
        loan.setTotalLoan(total);

        // Save loan
        loanRepository.save(loan);
        return loan;
    }

    // Find Loan by Id
    public Optional<LoanEntity> findById(Long id){
        return loanRepository.findById(id);
    }

    //Update Loan
    public LoanEntity updateLoan(LoanEntity loanEntity){
        return loanRepository.save(loanEntity);
    }

    // Delete Loam by id
    public boolean deleteLoan(Long id) throws Exception{
        try {
            loanRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // Get loan by rut client
    public List<LoanEntity> findByRutClient(String rut){
        return loanRepository.findByIdClient_RutClient(rut);
    }

    // Get tools by loan id
    public List<ToolEntity> getToolsByLoanId(Long id){
        try {
            Optional<LoanEntity> loan = loanRepository.findByIdLoan(id);
            return loan.get().getTool();
        }catch (Exception e){
            return null;
        }
    }

    // Get loans by id client
    public List<LoanEntity> findLoansByRutClient(String rut){
        return loanRepository.findByIdClient_RutClient(rut);
    }

    // Update penalty: This function updates the penalty of loan
    public LoanEntity updatePenaltyLoan(Long id){
        try{
            // Search loan by id
            LoanEntity loanEntity =  loanRepository.findById(id).orElse(null);
            // Search client by id
            ClientEntity client = loanEntity.getIdClient();
            // Search finish date of loan
            Date finishDate = loanEntity.getEndDate();
            // Search today date
            Date todayDate = new Date();

            // Verify if loan is finished
            if (loanEntity.getStateLoan().equals("FINALIZADO")) {
                return loanEntity;
            }

            // Verify if loan is finished and update penalty 
            if (finishDate.before(todayDate)) {
                long diffMillis = todayDate.getTime() - finishDate.getTime();
                long diffDays = diffMillis / (1000 * 60 * 60 * 24);

                List<ToolEntity> tools = loanEntity.getTool();

                int totalPenalty = 0;
                for (ToolEntity tool: tools) {
                    totalPenalty += tool.getLateCharge() * diffDays;
                    client.setStateClient("RESTRINGIDO");
                    clientRepository.save(client);
                }

                loanEntity.setPenaltyLoan(totalPenalty);
                loanRepository.save(loanEntity);
                return loanEntity;
            }
            loanEntity.setPenaltyLoan(0);
            return loanRepository.save(loanEntity);
        } catch (Exception e){
            return null;
        }
    }

    // finalize loan with conditions
    public LoanEntity finalizeLoan(Long id, int totalValueLoan) throws Exception {

        KardexEntity kardex = new KardexEntity();
        kardex.setDateKardex(new java.util.Date());


        try {
            LoanEntity loan = loanRepository.findById(id).get();

            // Search client by id
            ClientEntity client = clientRepository.findById(loan.getIdClient().getIdClient())
                    .orElseThrow(() -> new Exception("Cliente no encontrado"));

            // Search tools by loan id
            List<ToolEntity> completeTools = new ArrayList<>();
            for (ToolEntity t : loan.getTool()) {
                ToolEntity tool = toolRepository.findById(t.getIdTool()).orElseThrow(() -> new Exception("Herramienta no encontrada"));
                completeTools.add(tool);
            }

            // update stock tool and save kardex
            for (ToolEntity tool : completeTools) {
                tool.setStockTool(tool.getStockTool() + 1);
                toolRepository.save(tool);
                kardex.setIdTool(tool.getIdTool());
                kardex.setNameTool(tool.getNameTool());
                if (tool.getStockTool() == 1) {
                    tool.setStateTool("ACTIVA");
                }
                kardex.setStateTool("DEVOLUCIÓN");
                kardexRepository.save(kardex);
            }

            // Verify if client has debt
            if (client.getStateClient().equals("RESTRINGIDO")) {
                boolean hasDebt = false;
                List<LoanEntity> loans = clientRepository.findAllLoanByIdClient(client);
                for (LoanEntity l : loans) {
                    int penalty = l.getPenaltyLoan();
                    if (penalty > 0 && l.getStateLoan() == "ACTIVO") {
                        hasDebt = true;
                        break;
                    }
                }
                if (!hasDebt) {
                    client.setStateClient("ACTIVO");
                    clientRepository.save(client);
                }
            }

            // Update loan
            loan.setTotalLoan(totalValueLoan);
            loan.setStateLoan("FINALIZADO");
            return loanRepository.save(loan);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // Get all loans by date range
    public List<LoanEntity> getLoansByDateRange(Date startDate, Date endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new Exception("Las fechas no pueden ser nulas");
        }
        if (endDate.before(startDate)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        List<LoanEntity> allLoans = loanRepository.findAll();
        List<LoanEntity> filteredLoans = new ArrayList<>();

        for (LoanEntity loan : allLoans) {
            Date initDate = loan.getInitDate();
            if (initDate != null && !initDate.before(startDate) && !initDate.after(endDate)) {
                filteredLoans.add(loan);
            }
        }
        return filteredLoans;
    }

    //Get num loan "RESTRINGIDO" by rut client
    public Integer getNumLoanRestrinByRutClient(String rut) throws Exception {
        List<LoanEntity> loans = loanRepository.findByIdClient_RutClient(rut);
        int numRestrin = 0;
        for (LoanEntity loan : loans) {
            if (loan.getStateLoan().equals("RESTRINGIDO")) {
                numRestrin++;
            }
        }
        return numRestrin;
    }

    //Get num loan "ACTIVO" by rut client
    public Integer getNumActiveLoans(String rut) throws Exception {
        List<LoanEntity> loans = loanRepository.findByIdClient_RutClient(rut);
        int numActive = 0;
        for (LoanEntity loan : loans) {
            if (loan.getStateLoan().equals("ACTIVO")) {
                numActive++;
            }
        }
        return numActive;
    }
}
    