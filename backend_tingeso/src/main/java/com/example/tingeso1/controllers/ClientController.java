package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

     @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Get all clients
    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> listClients(){
        List<ClientEntity> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    // Get client by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClientEntity>> getClientByID(@PathVariable Long id){
        Optional<ClientEntity> client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    // Create client
    @PostMapping("/")
    public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity clientEntity){
        ClientEntity newClient = clientService.saveClient(clientEntity);
        return ResponseEntity.ok(newClient);
    }

    // Update client
    @PutMapping("/")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity clientEntity){
        ClientEntity updateClient = clientService.updateClient(clientEntity);
        return ResponseEntity.ok(updateClient);
    }

    // Delete client
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Get client by rut
    @GetMapping("/by-rut/{rut}")
    public ResponseEntity<ClientEntity> getClientByRut(@PathVariable String rut){
        ClientEntity client = clientService.getClientByRut(rut);
        return ResponseEntity.ok(client);
    }

    // Change state client
    @PutMapping("/change-state/{id}")
    public ResponseEntity<ClientEntity> changeStateClient(@PathVariable Long id) throws Exception{
        ClientEntity client = clientService.changeStateClient(id);
        return ResponseEntity.ok(client);
    }

    // Get restricted clients
    @GetMapping("/restricted-clients")
    public ResponseEntity<List<ClientEntity>> getRestrictedClients(){
        List<ClientEntity> restrictedClients = clientService.getRestrictedClients();
        return ResponseEntity.ok(restrictedClients);
    }
    
}
