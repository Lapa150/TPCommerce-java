package com.example.demo.services;

import com.example.demo.entities.Cart;
import com.example.demo.entities.Product;
import com.example.demo.repositories.CartsRepository;
import com.example.demo.repositories.ClientsRepository;
import com.example.demo.repositories.ProductsRepository;
import com.example.demo.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartsService {

    private final CartsRepository cartsRepository;
    private final ProductsRepository productsRepository;
    private final ClientsRepository clientsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository, ProductsRepository productsRepository, ClientsRepository clientsRepository) {
        this.cartsRepository = cartsRepository;
        this.productsRepository = productsRepository;
        this.clientsRepository = clientsRepository;
    }

    // Crear un nuevo carrito para un cliente
    public Cart createCartForClient(Long clientId) {
        Optional<Client> clientOpt = clientsRepository.findById(clientId);
        if (clientOpt.isPresent()) {
            Cart cart = new Cart();
            cart.setClient(clientOpt.get());
            return cartsRepository.save(cart);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // Crear o guardar un carrito
    public Cart saveCart(Cart cart) {
        return cartsRepository.save(cart);
    }

    // Obtener todos los carritos
    public List<Cart> getAllCarts() {
        return cartsRepository.findAll();
    }

    // Obtener un carrito por ID
    public Optional<Cart> getCartById(Long id) {
        return cartsRepository.findById(id);
    }

    // Eliminar un carrito por ID
    public void deleteCart(Long id) {
        cartsRepository.deleteById(id);
    }

    // Obtener el carrito de un cliente
    public Optional<Cart> getCartForClient(Client client) {
        return cartsRepository.findByClient(client);
    }

    // Añadir producto al carrito
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Optional<Cart> cartOpt = cartsRepository.findById(cartId);
        Optional<Product> productOpt = productsRepository.findById(productId);

        if (cartOpt.isPresent() && productOpt.isPresent()) {
            Cart cart = cartOpt.get();
            Product product = productOpt.get();

            cart.getProducts().add(product);
            cart.getQuantities().put(product.getId(), quantity);
            cart.calculateTotal();

            return cartsRepository.save(cart);
        } else {
            throw new RuntimeException("Carrito o producto no encontrado");
        }
    }

    // Eliminar producto del carrito
    public Cart removeProductFromCart(Long cartId, Long productId, int quantity) {
        Optional<Cart> cartOpt = cartsRepository.findById(cartId);
        Optional<Product> productOpt = productsRepository.findById(productId);

        if (cartOpt.isPresent() && productOpt.isPresent()) {
            Cart cart = cartOpt.get();
            Product product = productOpt.get();

            int currentQuantity = cart.getQuantities().getOrDefault(product.getId(), 0);
            if (currentQuantity >= quantity) {
                int newQuantity = currentQuantity - quantity;
                if (newQuantity == 0) {
                    cart.getProducts().remove(product);
                    cart.getQuantities().remove(product.getId());
                } else {
                    cart.getQuantities().put(product.getId(), newQuantity);
                }
                cart.calculateTotal();
                return cartsRepository.save(cart);
            } else {
                throw new RuntimeException("Cantidad a eliminar excede la cantidad en el carrito");
            }
        } else {
            throw new RuntimeException("Carrito o producto no encontrado");
        }
    }
}
