package com.example.tingeso1.services;

import com.example.tingeso1.entities.AdminEntity;
import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    // Find Client
    public ArrayList<ClientEntity> getClients(){
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    //Create Client
    public ClientEntity saveClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    // find Client by Id
    public ClientEntity getClientById(Long id){
        return clientRepository.findById(id).get();
    }

    //Find Client by Rut
    public ClientEntity getClientByRut(String rut){
        return clientRepository.findByRut_client(rut);
    }

    // Update Client
    public ClientEntity updateClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    // Delete Client
    public boolean deleteClient(Long id) throws  Exception{
        try {
            clientRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
