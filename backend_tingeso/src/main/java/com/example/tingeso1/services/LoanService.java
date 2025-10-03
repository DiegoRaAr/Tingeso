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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // Create Loan
    public LoanEntity createLoan(LoanEntity loan) throws Exception {
        List<ToolEntity> completeTools = new ArrayList<>();
        for (ToolEntity t : loan.getTool()) {
            ToolEntity tool = toolRepository.findById(t.getIdTool()).orElseThrow(() -> new Exception("Herramienta no encontrada"));
            completeTools.add(tool);
        }
        ClientEntity client = clientRepository.findById(loan.getIdClient().getIdClient())
        .orElseThrow(() -> new Exception("Cliente no encontrado"));

        boolean debt = false;
        List<LoanEntity> loans = clientRepository.findAllLoanByIdClient(client);
        for (LoanEntity l: loans) {
            int penalty = l.getPenaltyLoan();
            if (penalty > 0 && l.getStateLoan() == "ACTIVO") {
                debt = true;
                break;
            }
        }

        if (loans.size() >= 5) {
            throw new Exception("El cliente ya tiene 5 prestamos activos");
        }

        if (debt) {
            throw new Exception("El cliente tiene deudas pendientes");
        }

        System.out.println(client.getStateClient());

        if (client.getStateClient().equals("RESTRINGIDO")) {
            throw new Exception("El cliente se encuentra restringido");
        }

        Date iniDate = loan.getInitDate();
        Date endDate = loan.getEndDate();

        if (endDate.before(iniDate)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        for (ToolEntity tool: completeTools) {
            tool.setStockTool(tool.getStockTool() - 1);
            toolRepository.save(tool);

            if (tool.getStockTool() == 0) {
                tool.setStateTool("BAJA");
            }
            System.out.println("aaaaa");

            KardexEntity kardex = new KardexEntity();
            kardex.setDateKardex(new java.util.Date());
            kardex.setIdTool(tool.getIdTool());
            kardex.setNameTool(tool.getNameTool());

            kardex.setStateTool("PRESTAMO");

            kardexRepository.save(kardex);
        }
        loan.setHourLoan(LocalTime.now());
        loan.setTool(completeTools);
        loan.setIdClient(client);
        loanRepository.save(loan);
        return loan;
    }

    // Find Loan by Id
    public LoanEntity findById(Long id){
        return loanRepository.findById(id).get();
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
        LoanEntity loan = loanRepository.findByIdLoan(id);
        return loan.getTool();
    }

    // Get loans by id client
    public List<LoanEntity> findLoansByRutClient(String rut){
        return loanRepository.findByIdClient_RutClient(rut);
    }

    // Update penalty
    public LoanEntity updatePenaltyLoan(Long id){
        LoanEntity loanEntity = findById(id);
        ClientEntity client = loanEntity.getIdClient();
        Date finishDate = loanEntity.getEndDate();
        Date todayDate = new Date();

        if (loanEntity.getStateLoan().equals("FINALIZADO")) {
            return loanEntity;
        }

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
    }
}
