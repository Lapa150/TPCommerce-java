package com.example.demo.repositories;

import com.example.demo.entities.Client;
import com.example.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {


    // Método para buscar productos por su nombre
    List<Product> findByNameContainingIgnoreCase(String name);

    // Método para obtener productos dentro de un rango de precios
    List<Product> findByPriceBetween(double minPrice, double maxPrice);


}
