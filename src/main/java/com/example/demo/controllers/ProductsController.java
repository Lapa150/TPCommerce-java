package com.example.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.example.demo.entities.Product;
import com.example.demo.services.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    // Crear un nuevo producto
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        try {
            return productsService.saveProduct(product);
        } catch (Exception e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            throw new RuntimeException("Error al guardar producto", e);
        }
    }

    // Obtener todos los productos
    @GetMapping
    public List<Product> getAllProducts() {
        return productsService.getAllProducts();
    }

    // Obtener un producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productsService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productsService.deleteProduct(id);
            return ResponseEntity.ok("Producto eliminado con éxito");
        } catch (Exception exception) {
            // Mensaje si el id no existe
            System.err.println("Error borrando producto: " + exception.getMessage());
            return ResponseEntity.status(500).body("DELETE ERROR");
        }
    }

    // obtener productos dentro de un rango de precios
    @GetMapping("/price-range")
    public List<Product> findProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return productsService.findProductsByPriceBetween(minPrice, maxPrice);
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {
        try {
            String resultMessage = productsService.updateProduct(id, productDetails);
            if (resultMessage.equals("Product updated")) {
                return new ResponseEntity<>(resultMessage, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(resultMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
            return new ResponseEntity<>("Error updating product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
