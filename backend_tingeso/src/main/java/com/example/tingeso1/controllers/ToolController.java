package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.tools.Tool;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tool")
public class ToolController {
    @Autowired
    ToolService toolService;

    @GetMapping("/")
    public ResponseEntity<List<ToolEntity>> listTool(){
        List<ToolEntity> tools = toolService.getTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolEntity> getToolById(@PathVariable Long id){
        ToolEntity tool = toolService.findById(id);
        return ResponseEntity.ok(tool);
    }

    @PostMapping("/")
    public ResponseEntity<ToolEntity> saveTool(@RequestBody ToolEntity tool){
        ToolEntity newTool = toolService.createTool(tool);
        return ResponseEntity.ok(newTool);
    }

    @PutMapping("/")
    public ResponseEntity<ToolEntity> updateTool(@RequestBody ToolEntity tool){
        ToolEntity toolUpdated = toolService.updateTool(tool);
        return ResponseEntity.ok(toolUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTool(@PathVariable Long id) throws Exception {
        var isDeleted = toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/subtract-tool/{id}")
    public   ResponseEntity<Boolean> subtractToolNumber(@PathVariable Long id) throws Exception {
        toolService.subtractTool(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/add-tool/{id}")
    public   ResponseEntity<Boolean> addToolNumber(@PathVariable Long id) throws Exception {
        toolService.addTool(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/best-tools-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<List<ToolEntity>> getBestToolsByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date endDate
    ) {
        List<ToolEntity> bestTools = toolService.getBestToolsByRangeDate(initDate, endDate);
        return ResponseEntity.ok(bestTools);
    }
    
}
