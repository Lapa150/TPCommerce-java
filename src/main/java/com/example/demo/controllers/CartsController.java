package com.example.demo.controllers;

import com.example.demo.entities.Client;
import com.example.demo.services.ClientsService;
import com.example.demo.entities.Cart;
import com.example.demo.services.CartsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/carts")
public class CartsController {

    @Autowired
    private CartsService cartsService;

    @Autowired
    private ClientsService clientsService;





    // Crear un nuevo carrito para un cliente
    @PostMapping("/create/{clientId}")
    @Operation(summary = "Create cart",
            description = "Create a cart associated with a client id." )
    public ResponseEntity<String> createCart(@PathVariable Long clientId) {
        try {
            String message = cartsService.createCartForClient(clientId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Client not found");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating cart");
        }
    }

    // Obtener todos los carritos
    @GetMapping
    @Operation(summary = "Get all carts",
            description = "Shows all the carts with the associated client and their data" )
    public List<Cart> getAllCarts() {
        return cartsService.getAllCarts();
    }

    // Obtener un carrito por ID
    @GetMapping("/{id}")
    @Operation(summary = "Get cart by id.",
            description = "Shows a cart using its id. " )
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Optional<Cart> cart = cartsService.getCartById(id);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Eliminar un carrito por ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cart",
            description = "Deletes a cart using its id" )
    public ResponseEntity<String> deleteCart(@PathVariable Long id) {
        try {
            cartsService.deleteCart(id);
            return ResponseEntity.ok("Cart deleted succesfully");
        } catch (Exception exception) {
            System.err.println("Error deleting cart: " + exception.getMessage());
            return ResponseEntity.status(500).body("DELETE ERROR");
        }
    }

    // Añadir producto al carrito
    @PostMapping("/add/{cartId}/{productId}")
    @Operation(summary = "Add product to cart",
            description = "Using the cart id, the id of the product to be added is placed along with the quantity" )
    public ResponseEntity<String> addProductToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int quantity) {
        try {
            String resultMessage = cartsService.addProductToCart(cartId, productId, quantity);
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
            return new ResponseEntity<>("Error adding product to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar producto del carrito
    @DeleteMapping("/remove/{cartId}/{productId}")
    @Operation(summary = "Remove product from cart",
            description = "Uses the cart id and the product id to be removed completely" )
    public ResponseEntity<String> removeProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int quantity) {
        try {
            String resultMessage = cartsService.removeProductFromCart(cartId, productId, quantity);
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error removing product from cart: " + e.getMessage());
            return new ResponseEntity<>("Error removing product from cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Actualizar cantidad de producto en el carrito
    @PutMapping("/update/{cartId}/{productId}")
    @Operation(summary = "Update product quantity",
            description = "Uses the cart id and product id, then puts the new quantity of product." )
    public ResponseEntity<String> updateProductQuantity(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int newQuantity) {
        try {
            String resultMessage = cartsService.updateProductQuantity(cartId, productId, newQuantity);
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error updating product quantity in cart: " + e.getMessage());
            return new ResponseEntity<>("Error updating product quantity in cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Verificar si el carrito del cliente ha sido entregado
    @GetMapping("/{clientId}/isDelivered")
    @Operation(summary = "verify if cart is delivered",
            description = "shows if the cart was delivered or not" )
    public ResponseEntity<String> isCartDelivered(@PathVariable Long clientId) {
        try {
            String deliveryStatus = cartsService.isCartDelivered(clientId);
            return ResponseEntity.ok(deliveryStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    // Vaciar un carrito
    @DeleteMapping("/clear/{cartId}")
    @Operation(summary = "clean cart",
            description = "remove al products from the cart" )
    public ResponseEntity<String> clearCart(@PathVariable Long cartId) {
        try {
            String resultMessage = cartsService.clearCart(cartId);
            if ("Empty cart".equals(resultMessage)) {
                return new ResponseEntity<>(resultMessage, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(resultMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            return new ResponseEntity<>("Error clearing cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
