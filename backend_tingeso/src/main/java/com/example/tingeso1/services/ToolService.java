package com.example.tingeso1.services;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.repositories.KardexRepository;
import com.example.tingeso1.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    KardexRepository kardexRepository;

    // Find Tools
    public ArrayList<ToolEntity> getTools(){
        return (ArrayList<ToolEntity>) toolRepository.findAll();
    }

    // Create Tool
    public ToolEntity createTool(ToolEntity toolEntity){
        return toolRepository.save(toolEntity);
    }

    // Find Tool by id
    public ToolEntity findById(Long id){
        return toolRepository.findById(id).get();
    }

    // Update Tool
    public ToolEntity updateTool(ToolEntity toolEntity){
        return toolRepository.save(toolEntity);
    }

    // Delete Tool by Id
    public boolean deleteTool(Long id) throws Exception{
        try {
            toolRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // subtract tool by id
    public boolean subtractTool(Long id) throws Exception{
        ToolEntity tool =  findById(id);

        KardexEntity kardex = new KardexEntity();
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());

        // Verify if tool is the last one
        if (tool.getStockTool() == 1){
            tool.setStockTool(tool.getStockTool() - 1);
            tool.setStateTool("BAJA");
            kardex.setStateTool("BAJA");
            kardex.setNameTool(tool.getNameTool());
            kardexRepository.save(kardex);
            toolRepository.save(tool);
            return true;
        }
        // Verify if tool has stock 
        if (tool.getStockTool() > 1){
            tool.setStockTool(tool.getStockTool() - 1);
            kardex.setStateTool("DISMINUCIÓN");
            kardex.setNameTool(tool.getNameTool());
            kardexRepository.save(kardex);
            toolRepository.save(tool);
            return true;
        }
        throw new Exception("Action not found (subtract tool)");
    }

    // Add tool by id
    public boolean addTool(Long id){
        ToolEntity tool =  findById(id);
        KardexEntity kardex = new KardexEntity();

        // Update the kardex
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());
        kardex.setStateTool("SUMA");
        kardex.setNameTool(tool.getNameTool());
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
