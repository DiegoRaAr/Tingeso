package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity,Long> {
    public KardexEntity findByIdKardex(Long id);

    List<KardexEntity> findByDateKardexBetween(Date startdate, Date enddate);
}
