package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("SELECT c FROM ClientEntity c WHERE c.rutClient = :rut")
    public ClientEntity findByRutClient(@Param("rut") String rut);

    public ClientEntity findByIdClient(Long id);

    @Query("SELECT l FROM LoanEntity l WHERE l.idClient = :client")
    List<LoanEntity> findAllLoanByIdClient(ClientEntity client);

    public ClientEntity findByNameClient(String name);
    public ClientEntity findAByStateClient(String state);

}
