package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.ToolEntity;
import com.example.tingeso1.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tool")
public class ToolController {
    private final ToolService toolService;

    @Autowired
    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    // Get all tools
    @GetMapping("/")
    public ResponseEntity<List<ToolEntity>> listTool(){
        List<ToolEntity> tools = toolService.getTools();
        return ResponseEntity.ok(tools);
    }

    // Get tool by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ToolEntity>> getToolById(@PathVariable Long id){
        Optional<ToolEntity> tool = toolService.findById(id);
        return ResponseEntity.ok(tool);
    }

    // Create tool
    @PostMapping("/")
    public ResponseEntity<ToolEntity> saveTool(@RequestBody ToolEntity tool){
        ToolEntity newTool = toolService.createTool(tool);
        return ResponseEntity.ok(newTool);
    }

    // Update tool
    @PutMapping("/")
    public ResponseEntity<ToolEntity> updateTool(@RequestBody ToolEntity tool){
        ToolEntity toolUpdated = toolService.updateTool(tool);
        return ResponseEntity.ok(toolUpdated);
    }

    // Delete tool by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTool(@PathVariable Long id) {
        toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }

    // Subtract tool number
    @PutMapping("/subtract-tool/{id}")
    public ResponseEntity<Boolean> subtractToolNumber(@PathVariable Long id) {
        toolService.subtractTool(id);
        return ResponseEntity.noContent().build();
    }

    // Add tool number
    @PutMapping("/add-tool/{id}")
    public ResponseEntity<Boolean> addToolNumber(@PathVariable Long id){
        toolService.addTool(id);
        return ResponseEntity.noContent().build();
    }

    // Get best tools by range date
    @GetMapping("/best-tools-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<List<ToolEntity>> getBestToolsByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date endDate
    ) {
        List<ToolEntity> bestTools = toolService.getBestToolsByRangeDate(initDate, endDate);
        return ResponseEntity.ok(bestTools);
    }
}
