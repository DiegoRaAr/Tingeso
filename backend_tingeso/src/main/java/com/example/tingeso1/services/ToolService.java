package com.example.tingeso1.services;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.exceptions.DataPersistenceException;
import com.example.tingeso1.exceptions.ResourceNotFoundException;
import com.example.tingeso1.repositories.KardexRepository;
import com.example.tingeso1.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ToolService {
    private final ToolRepository toolRepository;
    private final KardexRepository kardexRepository;

    private static final String TOOL_NOT_FOUND_MESSAGE = "Herramienta no encontrada";

    @Autowired
    public ToolService(ToolRepository toolRepository, KardexRepository kardexRepository) {
        this.toolRepository = toolRepository;
        this.kardexRepository = kardexRepository;
    }

    // Find Tools
    public ArrayList<ToolEntity> getTools() {
        return (ArrayList<ToolEntity>) toolRepository.findAll();
    }

    // Create Tool
    public ToolEntity createTool(ToolEntity toolEntity) {
        ToolEntity savedTool = toolRepository.save(toolEntity);

        String stock = String.valueOf(savedTool.getStockTool());
        
        // Registrar en kardex como "NUEVA"
        KardexEntity kardex = new KardexEntity();
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(savedTool.getIdTool());
        kardex.setNameTool(savedTool.getNameTool());
        kardex.setStateTool("NUEVA +" + stock);
        kardexRepository.save(kardex);
        
        return savedTool;
    }

    // Find Tool by id
    public Optional<ToolEntity> findById(Long id) {
        return toolRepository.findById(id);
    }

    // Update Tool
    public ToolEntity updateTool(ToolEntity toolEntity) {
        int actualStock = findById(toolEntity.getIdTool())
                .orElseThrow(() -> new ResourceNotFoundException(TOOL_NOT_FOUND_MESSAGE))
                .getStockTool();
        int newStock = toolEntity.getStockTool();

        KardexEntity kardex = new KardexEntity();
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(toolEntity.getIdTool());
        kardex.setNameTool(toolEntity.getNameTool());
        if (newStock > actualStock) {
            kardex.setStateTool("SUMA +" + (newStock - actualStock));
        } else if (newStock < actualStock) {
            kardex.setStateTool("DISMINUCIÓN -" + (actualStock - newStock));
        } else {
            kardex.setStateTool("ACTUALIZACIÓN");
        }
        kardexRepository.save(kardex);
        return toolRepository.save(toolEntity);
    }

    // Delete Tool by Id
    public boolean deleteTool(Long id) {
        try {
            toolRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DataPersistenceException("Error al eliminar la herramienta: " + e.getMessage(), e);
        }
    }

    // subtract tool by id
    public boolean subtractTool(Long id) {
        ToolEntity tool = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TOOL_NOT_FOUND_MESSAGE));

        KardexEntity kardex = new KardexEntity();
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());

        // Verify if tool is the last one
        if (tool.getStockTool() == 1) {
            tool.setStockTool(tool.getStockTool() - 1);
            tool.setStateTool("BAJA");
            kardex.setStateTool("BAJA");
            kardex.setNameTool(tool.getNameTool());
            kardexRepository.save(kardex);
            toolRepository.save(tool);
            return true;
        }
        // Verify if tool has stock 
        if (tool.getStockTool() > 1) {
            tool.setStockTool(tool.getStockTool() - 1);
            kardex.setStateTool("DISMINUCIÓN");
            kardex.setNameTool(tool.getNameTool());
            kardexRepository.save(kardex);
            toolRepository.save(tool);
            return true;
        }
        throw new IllegalStateException("No se puede restar stock a una herramienta sin stock");
    }

    // Add tool by id
    public boolean addTool(Long id) {
        ToolEntity tool = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TOOL_NOT_FOUND_MESSAGE));
        KardexEntity kardex = new KardexEntity();

        // Update the kardex
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());
        kardex.setNameTool(tool.getNameTool());
        
        // Si el stock es 0, el estado es "NUEVA", sino es "SUMA"
        if (tool.getStockTool() == 0) {
            kardex.setStateTool("NUEVA");
            tool.setStateTool("ACTIVA");
        } else {
            kardex.setStateTool("SUMA");
        }
        
        kardexRepository.save(kardex);
        tool.setStockTool(tool.getStockTool() + 1);
        toolRepository.save(tool);
        return true;
    }

    // Get best tools by range date
    public List<ToolEntity> getBestToolsByRangeDate(java.util.Date initDate, java.util.Date endDate) {
        // Get all kardex between the given dates
        List<KardexEntity> kardexList = kardexRepository.findByDateKardexBetween(initDate, endDate);

        // filter for "PRESTAMO" state
        List<KardexEntity> prestamos = kardexList.stream()
                .filter(k -> "PRESTAMO".equals(k.getStateTool()))
                .toList();

        // Count occurrences of each tool ID
        Map<Long, Integer> toolCount = new HashMap<>();
        for (KardexEntity kardex : prestamos) {
            Long toolId = kardex.getIdTool();
            toolCount.put(toolId, toolCount.getOrDefault(toolId, 0) + 1);
        }

        // Order tool IDs by their count in descending order
        List<Long> sortedToolIds = toolCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        // get ToolEntity objects for the sorted IDs
        List<ToolEntity> bestTools = new ArrayList<>();
        for (Long toolId : sortedToolIds) {
            toolRepository.findById(toolId).ifPresent(bestTools::add);
        }

        return bestTools;
    }

}
