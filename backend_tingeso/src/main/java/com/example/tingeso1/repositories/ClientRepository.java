package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("SELECT c FROM ClientEntity c WHERE c.rutClient = :rut")
    public ClientEntity findByRutClient(@Param("rut") String rut);

    public ClientEntity findByNameClient(String name);
    public ClientEntity findAByStateClient(String state);

}
