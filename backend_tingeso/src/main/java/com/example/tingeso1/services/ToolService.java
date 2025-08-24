package com.example.tingeso1.services;

import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;

    // Dind Tools
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

    // Delete Tool
    public boolean deleteTool(ToolEntity toolEntity) throws Exception{
        try {
            toolRepository.delete(toolEntity);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
