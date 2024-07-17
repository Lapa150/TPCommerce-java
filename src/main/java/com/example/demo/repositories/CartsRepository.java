package com.example.demo.repositories;

import com.example.demo.entities.Cart;
import com.example.demo.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartsRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByClient(Client client);
}
