package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.exceptions.DataPersistenceException;
import com.example.tingeso1.exceptions.ResourceNotFoundException;
import com.example.tingeso1.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Find Client
    public ArrayList<ClientEntity> getClients() {
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    //Create Client
    public ClientEntity saveClient(ClientEntity clientEntity) {
        return clientRepository.save(clientEntity);
    }

    // find Client by Id
    public Optional<ClientEntity> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    //Find Client by Rut
    public ClientEntity getClientByRut(String rut) {
        return clientRepository.findByRutClient(rut);
    }

    // Update Client
    public ClientEntity updateClient(ClientEntity clientEntity) {
        return clientRepository.save(clientEntity);
    }

    // Delete Client
    public boolean deleteClient(Long id) {
        try {
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DataPersistenceException("Error deleting client with id: " + id, e);
        }
    }

    // Change State Client
    public ClientEntity changeStateClient(Long id) throws Exception {
        Optional<ClientEntity> clientOptional = getClientById(id);
        if (clientOptional.isEmpty()) {
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }
        ClientEntity client = clientOptional.get();
        if (client.getStateClient().equals("ACTIVO")) {
            client.setStateClient("RESTRINGIDO");
        } else {
            client.setStateClient("ACTIVO");
        }
        return clientRepository.save(client);
    }

    // Get client with state "RESTRINGIDO"
    public ArrayList<ClientEntity> getRestrictedClients() {
        List<ClientEntity> clients = clientRepository.findAll();

        ArrayList<ClientEntity> restrictedClients = new ArrayList<>();
        for (ClientEntity client: clients) {
            if (client.getStateClient().equals("RESTRINGIDO")) {
                restrictedClients.add(client);
            }
        }
        return restrictedClients;
    }
}
