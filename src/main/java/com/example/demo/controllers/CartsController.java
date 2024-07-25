package com.example.demo.controllers;


import com.example.demo.entities.Cart;
import com.example.demo.services.CartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/carts")
public class CartsController {

    @Autowired
    private CartsService cartsService;

    // Crear un nuevo carrito y asociarlo con un cliente
    @PostMapping("/create/{clientId}")
    public Cart createCart(@PathVariable Long clientId) {
        try {
            return cartsService.createCartForClient(clientId);
        } catch (Exception e) {
            System.err.println("Error al guardar carrito: " + e.getMessage());
            throw new RuntimeException("Error al guardar carrito", e);
        }
    }

    // Obtener todos los carritos
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartsService.getAllCarts();
    }

    // Obtener un carrito por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Optional<Cart> cart = cartsService.getCartById(id);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Eliminar un carrito por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable Long id) {
        try {
            cartsService.deleteCart(id);
            return ResponseEntity.ok("Carrito eliminado con éxito");
        } catch (Exception exception) {
            System.err.println("Error borrando carrito: " + exception.getMessage());
            return ResponseEntity.status(500).body("DELETE ERROR");
        }
    }

    // Añadir producto al carrito
    @PostMapping("/{cartId}/addProduct/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        try {
            Cart updatedCart = cartsService.addProductToCart(cartId, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Eliminar una cantidad específica de un producto del carrito
    @PostMapping("/{cartId}/removeProduct/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        try {
            Cart updatedCart = cartsService.removeProductFromCart(cartId, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Actualizar cantidad de un producto en el carrito
    @PutMapping("/{cartId}/updateProduct/{productId}")
    public ResponseEntity<Cart> updateProductQuantity(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int newQuantity) {
        try {
            Cart updatedCart = cartsService.updateProductQuantity(cartId, productId, newQuantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
