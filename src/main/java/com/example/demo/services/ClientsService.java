package com.example.demo.services;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientsRepository;
import com.example.demo.repositories.CartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {

     @Autowired
     private ClientsRepository clientsRepository;

     @Autowired
     private CartsRepository cartsRepository;


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

     //Mostrar cantidad de clientes total
     public long countClients() {
          return clientsRepository.count();
     }

     //Buscar cliente por email
     public Optional<Client> getClientByEmail(String email) {
          return clientsRepository.findByEmail(email);
     }



     //Obtener cliente por nombre
     public List<Client> findClientsByName(String name) {
          return clientsRepository.findByNameContainingIgnoreCase(name);
     }


     // Mostrar clientes con carritos pendientes a entregar
     //Stream funciona para convertir la lista en un flujo para procesar
     //collectors hace que los elementos filtrados devuelvan una lista
     public List<Client> getClientsWithPendingCarts() {
          return clientsRepository.findAll().stream()
                  .filter(client -> !cartsRepository.findByClientAndDeliveredFalse(client).isEmpty())
                  .collect(Collectors.toList());
     }



     //Eliminar un cliente por id
     public void deleteClient(Long id) {
          clientsRepository.deleteById(id);
     }

     // Actualizar un cliente existente
     public Optional<Client> updateClient(Long id, Client clientDetails) {
          return clientsRepository.findById(id).map(existingClient -> {
               existingClient.setName(clientDetails.getName());
               existingClient.setDni(clientDetails.getDni());
               existingClient.setEmail(clientDetails.getEmail());

               return clientsRepository.save(existingClient);
          });
     }

     // Eliminar todos los clientes
     public void deleteAllClients() {
          clientsRepository.deleteAll();
     }

}
