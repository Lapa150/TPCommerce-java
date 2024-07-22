package com.example.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.example.demo.entities.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/clients")
public class ClientsController {

    @Autowired
    private ClientsService clientsService;

    // Crear un nuevo cliente
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        try {
            return clientsService.createClient(client);
        } catch (Exception e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
            throw new RuntimeException("Error al guardar cliente", e);
        }
    }

    // Obtener todos los clientes
    @GetMapping
    public List<Client> getAllClients() {
        return clientsService.getAllClients();
    }

    // Obtener un cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientsService.getClientById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        try {
            clientsService.deleteClient(id);
            return ResponseEntity.ok("Client deleted successfully");
        } catch (Exception exception) {
            // Mensaje si el id no existe
            System.err.println("Error deleting client: " + exception.getMessage());
            return ResponseEntity.status(500).body("DELETE ERROR");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        try {
            Optional<Client> updatedClient = clientsService.updateClient(id, clientDetails);
            return updatedClient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
        } catch (Exception e) {
            System.err.println("Error actualizando cliente: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
