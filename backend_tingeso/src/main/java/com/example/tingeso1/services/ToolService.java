package com.example.tingeso1.services;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.repositories.KardexRepository;
import com.example.tingeso1.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

        if (tool.getStockTool() == 1){
            tool.setStockTool(tool.getStockTool() - 1);
            tool.setStateTool("BAJA");
            kardex.setStateTool("BAJA");
            kardexRepository.save(kardex);
            toolRepository.save(tool);
            return true;
        }
        if (tool.getStockTool() > 1){
            tool.setStockTool(tool.getStockTool() - 1);
            kardex.setStateTool("DISMINUCIÓN");
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

        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());
        kardex.setStateTool("SUMA");
        kardexRepository.save(kardex);
        tool.setStockTool(tool.getStockTool() + 1);
        toolRepository.save(tool);
        return true;
    }

}
