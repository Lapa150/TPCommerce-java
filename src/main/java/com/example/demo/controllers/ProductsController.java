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


    @PostMapping
    @Operation(summary = "Create new product",
            description = "Creates a new product with name, price and stock." )
    public Product createProduct(@RequestBody Product product) {
        try {
            return productsService.saveProduct(product);
        } catch (Exception e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            throw new RuntimeException("Error al guardar producto", e);
        }
    }


    @GetMapping
    @Operation(summary = "Show all products",
            description = "Shows a list with all the products and their data" )
    public List<Product> getAllProducts() {
        return productsService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by Id",
            description = "Shows a product data using their id" )
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productsService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

   
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product",
            description = "Delete a product using their id." )
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
    @Operation(summary = "Get products in a price range",
            description = "set the minimum price and maximum price to show the products within that parameter." )
    public List<Product> findProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return productsService.findProductsByPriceBetween(minPrice, maxPrice);
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    @Operation(summary = "Update product",
            description = "Allows editing the name, price and stock of the product selected by its id." )
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
