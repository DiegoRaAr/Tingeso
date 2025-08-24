package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity,Long> {
    public KardexEntity findById_kardex(Long id);

    List<KardexEntity> findByDate_kardexBetween(Date startdate, Date enddate);
}
