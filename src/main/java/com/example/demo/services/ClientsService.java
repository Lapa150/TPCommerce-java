package com.example.demo.services;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {

     @Autowired
     private ClientsRepository clientsRepository;


     //Guardar un cliente creado.
     public Client createClient(Client client) {
          return clientsRepository.save(client);
     }

     // Obtener todos los clientes
     public List<Client> getAllClients() {
          return clientsRepository.findAll();
     }

     // Obtener cliente por id
     public Optional<Client> getClientById(Long id) {
          return clientsRepository.findById(id);
     }


     //Eliminar un cliente por id
     public void deleteClient(Long id) {
          clientsRepository.deleteById(id);
     }

     // Actualizar un cliente existente
     public Optional<Client> updateClient(Long id, Client clientDetails) {
          return clientsRepository.findById(id).map(existingClient -> {
               existingClient.setName(clientDetails.getName());
               // Actualiza otros campos según sea necesario
               return clientsRepository.save(existingClient);
          });
     }









}
