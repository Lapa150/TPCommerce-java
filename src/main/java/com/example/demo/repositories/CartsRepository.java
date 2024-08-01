package com.example.demo.repositories;

import com.example.demo.entities.Cart;
import com.example.demo.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CartsRepository extends JpaRepository<Cart, Long> {

    //Metodo para obtener un carrito por nombre de cliente
    Optional<Cart> findByClient(Client client);

    // Lista que devuelve clientes con carritos sin entregar
    List<Cart> findByClientAndDeliveredFalse(Client client);
}
