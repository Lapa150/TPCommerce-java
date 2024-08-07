package com.example.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.demo.entities.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping(path = "api/v1/clients")
@Tag(name= "Controller for clients", description = "Methods for clients")
public class ClientsController {

    @Autowired
    private ClientsService clientsService;

    // Crear un nuevo cliente
    @PostMapping
    @Operation(summary = "create client",
            description = "Save a client with name, dni and email." )
    public Client createClient(@RequestBody Client client) {
        try {
            return clientsService.createClient(client);
        } catch (Exception e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
            throw new RuntimeException("Error al guardar cliente", e);
        }
    }


    @GetMapping
    @Operation(summary = "Show all clients",
            description = "Generate a list for all clients in the repository with their data" )
    public List<Client> getAllClients() {
        return clientsService.getAllClients();
    }

    // Obtener un cliente por id
    @GetMapping("/{id}")
    @Operation(summary = "Get a client using an id",
            description = "Shows all the data about the client" )
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientsService.getClientById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Mostrar la cantidad de clientes total
    @GetMapping("/count")
    @Operation(summary = "Show the total number of clients",
            description = "Shows in a number how many clients there are in the repository." )
    public ResponseEntity<String> getClientCount() {
        long count = clientsService.countClients();
        String message = "Clients in the application: " + count;
        return ResponseEntity.ok(message);
    }


    // Buscar un cliente por email
    @GetMapping("/by-email")
    @Operation(summary = "Search for a client by email",
            description = "If the email exist in the repository, its respective client will be seen." )
    public ResponseEntity<Client> getClientByEmail(@RequestParam String email) {
        Optional<Client> client = clientsService.getClientByEmail(email);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Buscar clientes por nombre
    @GetMapping("/search")
    @Operation(summary = "Search clients by name",
            description = "If the name exist in the repository, its respective client will be seen." )
    public List<Client> findClientsByName(@RequestParam String name) {
        return clientsService.findClientsByName(name);
    }

    // Obtener clientes con carritos pendientes
    @GetMapping("/with-pending-carts")
    @Operation(summary = "Get client with pending carts",
            description = "Shows all the clients with undelivered carts" )
    public ResponseEntity<?> getClientsWithPendingCarts() {
        List<Client> clients = clientsService.getClientsWithPendingCarts();
        if (clients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No clients with pending carts.");
        }
        return ResponseEntity.ok(clients);
    }

    // Eliminar un cliente por id
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by id",
            description = "Delete a client with their carts and invoices" )
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

    // Actualizar cliente
    @PutMapping("/{id}")
    @Operation(summary = "Update client",
            description = "allows edit the name, dni and email." )
    public ResponseEntity<Map<String, String>> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        try {
            Optional<Client> updatedClient = clientsService.updateClient(id, clientDetails);
            if (updatedClient.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Client updated");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(404).body(Collections.singletonMap("message", "Client not found"));
            }
        } catch (Exception e) {
            System.err.println("Error updating client: " + e.getMessage());
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "Error updating client"));
        }
    }


    // Endpoint para eliminar todos los clientes
    @DeleteMapping
    @Operation(summary = "Delete all clients",
            description = "" )
    public ResponseEntity<String> deleteAllClients() {
        clientsService.deleteAllClients();
        return new ResponseEntity<>("All clients have been deleted", HttpStatus.OK);
    }

}
