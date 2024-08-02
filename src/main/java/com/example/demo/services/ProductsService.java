package com.example.demo.services;


import com.example.demo.repositories.ProductsRepository;
import com.example.demo.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    //Guardar un producto creado.
    public Product saveProduct(Product product) {
        return productsRepository.save(product);
    }

    // Obtener todos los productos
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    // Obtener producto por id
    public Optional<Product> getProductById(Long id) {
        return productsRepository.findById(id);
    }


    //Eliminar un producto por id
    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }


    // Buscar productos por nombre (ignora mayúsculas y minúsculas)
    public List<Product> findProductsByName(String name) {
        return productsRepository.findByNameContainingIgnoreCase(name);
    }

    // Obtener el conteo total de productos
    public long countProducts() {
        return productsRepository.count();
    }

    // Obtener productos dentro de un rango de precios
    public List<Product> findProductsByPriceBetween(double minPrice, double maxPrice) {
        return productsRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Actualizar un producto
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productsRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            return productsRepository.save(existingProduct);
        });
    }




}
