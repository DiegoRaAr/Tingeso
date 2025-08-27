package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findByRutClient(String rut);
    public ClientEntity findByNameClient(String name);

    public ClientEntity findAByStateClient(String state);

}
