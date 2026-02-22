package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/kardex")
public class KardexController {
    
    private final KardexService kardexService;

    @Autowired
    public KardexController(KardexService kardexService) {
        this.kardexService = kardexService;
    }

    // Get all kardexes
    @GetMapping("/")
    public ResponseEntity<List<KardexEntity>> listKardexes(){
        List<KardexEntity> kardex = kardexService.getKardexes();
        return ResponseEntity.ok(kardex);
    }

    // Get kardex by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<KardexEntity>> getKardexByID(@PathVariable Long id){
        Optional<KardexEntity> kardex = kardexService.getKardexById(id);
        return ResponseEntity.ok(kardex);
    }

    // Create kardex
    @PostMapping("/")
    public ResponseEntity<KardexEntity> saveKardex(@RequestBody KardexEntity kardexEntity){
        KardexEntity newKardex = kardexService.createKardex(kardexEntity);
        return   ResponseEntity.ok(newKardex);
    }

    // Update kardex
    @PutMapping("/")
    public ResponseEntity<KardexEntity> updateKardex(@RequestBody KardexEntity kardexEntity){
        KardexEntity kardexUpdate = kardexService.updateKardex(kardexEntity);
        return ResponseEntity.ok(kardexUpdate);
    }

    // Delete kardex by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteKardexByID(@PathVariable Long id){
        kardexService.deleteKardex(id);
        return ResponseEntity.noContent().build();
    }
}
