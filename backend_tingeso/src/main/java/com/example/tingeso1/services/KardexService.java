package com.example.tingeso1.services;

import com.example.tingeso1.entities.KardexEntity;
import com.example.tingeso1.exceptions.DataPersistenceException;
import com.example.tingeso1.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class KardexService {
    private final KardexRepository kardexRepository;

    @Autowired
    public KardexService(KardexRepository kardexRepository) {
        this.kardexRepository = kardexRepository;
    }

    // Find Kardex's
    public ArrayList<KardexEntity> getKardexes() {
        return (ArrayList<KardexEntity>) kardexRepository.findAll();
    }

    // Create Kardex
    public KardexEntity createKardex(KardexEntity kardexEntity) {
        return kardexRepository.save(kardexEntity);
    }

    // Find Kardex by Id
    public Optional<KardexEntity> getKardexById(Long id) {
        return kardexRepository.findById(id);
    }

    // Update Kardex
    public KardexEntity updateKardex(KardexEntity kardexEntity) {
        return kardexRepository.save(kardexEntity);
    }

    // Delete Kardex
    public boolean deleteKardex(Long id) {
        try {
            kardexRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DataPersistenceException("Error deleting kardex with id: " + id, e);
        }
    }
}
