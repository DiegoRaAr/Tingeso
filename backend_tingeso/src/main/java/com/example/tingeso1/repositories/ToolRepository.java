package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {
    public ToolEntity findByNameTool(String name);
    public ToolEntity findByIdTool(long id);

    List<ToolEntity> findByCategoryTool(String category);
    List<ToolEntity> findByStateTool(String state);
    List<ToolEntity> findByDailyCharge(int daily_charge);
}
