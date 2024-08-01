package com.example.demo.repositories;

import com.example.demo.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Long> {


    // Metodo que devuelve ture o false si un cliente existe mediante un email.
    boolean existsByEmail(String email);

    // Método para buscar clientes por nombre, ignorando mayúsculas y minúsculas
    List<Client> findByNameContainingIgnoreCase(String name);
}
