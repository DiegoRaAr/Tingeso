package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findByRut_client(String rut);
    public ClientEntity findByName_client(String name);

    public ClientEntity findAByState_client(String state);

}
