package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {
    public ToolEntity findByName_tool(String name);
    public ToolEntity findById_tool(long id);

    List<ToolEntity> findByCategory_tool(String category);
    List<ToolEntity> findByState_tool(String state);
    List<ToolEntity> findByDaily_charge(int daily_charge);
}
