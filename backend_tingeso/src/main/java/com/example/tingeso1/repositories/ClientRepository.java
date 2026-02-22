package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Class ClientRepository, this class extends JpaRepository
// and is used to interact with the client table in the database.
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    // Function to find client by rut.
    // Input: rut of client(String)
    // Output: client entity
    @Query("SELECT c FROM ClientEntity c WHERE c.rutClient = :rut")
    public ClientEntity findByRutClient(@Param("rut") String rut);

    // Function to find client by id.
    // Input: id of client(Long)
    // Output: client entity
    public ClientEntity findByIdClient(Long id);

    // Function to find all loans by client.
    // Input: client entity
    // Output: list of loan entities
    @Query("SELECT l FROM LoanEntity l WHERE l.idClient = :client")
    List<LoanEntity> findAllLoanByIdClient(ClientEntity client);

    // Function to find client by name.
    // Input: name of client(String)
    // Output: client entity
    public ClientEntity findByNameClient(String name);

    // Function to find client by state.
    // Input: state of client(String)
    // Output: list of client entities
    public ClientEntity findAByStateClient(String state);

}
