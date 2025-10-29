package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kardex")
public class KardexController {
    @Autowired
    KardexService kardexService;

    @GetMapping("/")
    public ResponseEntity<List<KardexEntity>> listKardexes(){
        List<KardexEntity> kardex = kardexService.getKardexes();
        return ResponseEntity.ok(kardex);
    }

    @GetMapping("/id")
    public ResponseEntity<KardexEntity> getKardexByID(@PathVariable Long id){
        KardexEntity kardex = kardexService.getKardexById(id);
        return ResponseEntity.ok(kardex);
    }

    @PostMapping("/")
    public ResponseEntity<KardexEntity> saveKardex(@RequestBody KardexEntity kardexEntity){
        KardexEntity newKardex = kardexService.createKardex(kardexEntity);
        return   ResponseEntity.ok(newKardex);
    }

    @PutMapping("/")
    public ResponseEntity<KardexEntity> updateKardex(@RequestBody KardexEntity kardexEntity){
        KardexEntity kardexUpdate = kardexService.updateKardex(kardexEntity);
        return ResponseEntity.ok(kardexUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteKardexByID(@PathVariable Long id)throws Exception{
        var isDeleted = kardexService.deleteKardex(id);
        return ResponseEntity.noContent().build();
    }
}
