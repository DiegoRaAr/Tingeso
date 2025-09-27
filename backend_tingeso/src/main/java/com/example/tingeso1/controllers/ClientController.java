package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.ClientEntity;
import com.example.tingeso1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> listClients(){
        List<ClientEntity> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientByID(@PathVariable Long id){
        ClientEntity client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/")
    public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity clientEntity){
        ClientEntity newClient = clientService.saveClient(clientEntity);
        return ResponseEntity.ok(newClient);
    }

    @PutMapping("/")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity clientEntity){
        ClientEntity updateClient = clientService.updateClient(clientEntity);
        return ResponseEntity.ok(updateClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable Long id) throws Exception{
        var isDeleted = clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-rut/{rut}")
    public ResponseEntity<ClientEntity> getClientByRut(@PathVariable String rut){
        ClientEntity client = clientService.getClientByRut(rut);
        return ResponseEntity.ok(client);
    }
    
}
