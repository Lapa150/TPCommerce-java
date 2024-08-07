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
    public String createCartForClient(Long clientId) {
        Optional<Client> client = clientsRepository.findById(clientId);
        if (client.isPresent()) {
            Cart cart = new Cart();
            cart.setClient(client.get());
            cartsRepository.save(cart);
            return "Cart created for client: " + client.get().getName();
        } else {
            throw new RuntimeException("Client not found");
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
    public String addProductToCart(Long cartId, Long productId, int quantity) {
        Optional<Cart> cartUsed = cartsRepository.findById(cartId);
        Optional<Product> productToDeposit = productsRepository.findById(productId);

        if (cartUsed.isPresent() && productToDeposit.isPresent()) {
            Cart cart = cartUsed.get();
            Product product = productToDeposit.get();

            if (product.getStock() >= quantity) {
                cart.getProducts().add(product);
                cart.getQuantities().put(product.getId(), quantity);
                product.setStock(product.getStock() - quantity);
                cart.calculateTotal();

                productsRepository.save(product);
                cartsRepository.save(cart);
                return "Products added";
            } else {
                throw new RuntimeException("Not enough stock");
            }
        } else {
            throw new RuntimeException("Cart or product not found");
        }
    }


    // Eliminar producto del carrito
    public String removeProductFromCart(Long cartId, Long productId, int quantity) {
        Optional<Cart> cartToUpdate = cartsRepository.findById(cartId);
        Optional<Product> productToUpdate = productsRepository.findById(productId);

        if (cartToUpdate.isPresent() && productToUpdate.isPresent()) {
            Cart cart = cartToUpdate.get();
            Product product = productToUpdate.get();

            int currentQuantity = cart.getQuantities().getOrDefault(product.getId(), 0);
            if (currentQuantity >= quantity) {
                int newQuantity = currentQuantity - quantity;
                if (newQuantity == 0) {
                    cart.getProducts().remove(product);
                    cart.getQuantities().remove(product.getId());
                } else {
                    cart.getQuantities().put(product.getId(), newQuantity);
                }

                // Sumar cantidad al stock
                product.setStock(product.getStock() + quantity);
                productsRepository.save(product);

                cart.calculateTotal();
                cartsRepository.save(cart);

                return "Product deleted from cart";
            } else {
                throw new RuntimeException("The quantity to be removed exceeds the quantity in the cart");
            }
        } else {
            throw new RuntimeException("Cart or product not found");
        }
    }


    // Actualizar cantidad de producto en el carrito
    public String updateProductQuantity(Long cartId, Long productId, int newQuantity) {
        Optional<Cart> cartToUpdate = cartsRepository.findById(cartId);
        Optional<Product> productToUpdate = productsRepository.findById(productId);

        if (cartToUpdate.isPresent() && productToUpdate.isPresent()) {
            Cart cart = cartToUpdate.get();
            Product product = productToUpdate.get();

            int currentQuantity = cart.getQuantities().getOrDefault(product.getId(), 0);

            // Ajustar el stock del producto según la nueva cantidad
            int stockChange = newQuantity - currentQuantity;
            if (product.getStock() >= stockChange) {
                cart.getQuantities().put(product.getId(), newQuantity);
                product.setStock(product.getStock() - stockChange);
                productsRepository.save(product);

                cart.calculateTotal();
                cartsRepository.save(cart);

                return "Product updated in cart";
            } else {
                throw new RuntimeException("Not enough stock to update quantity");
            }
        } else {
            throw new RuntimeException("Cart or product not found");
        }
    }


    // Verificar si el carrito del cliente ha sido entregado
    public String isCartDelivered(Long clientId) {
        Optional<Client> clientOpt = clientsRepository.findById(clientId);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            Optional<Cart> cartOpt = getCartForClient(client);
            if (cartOpt.isPresent()) {
                boolean isDelivered = cartOpt.get().isDelivered();
                if (isDelivered) {
                    return "Cart delivered";
                } else {
                    return "Cart not delivered";
                }
            } else {
                throw new RuntimeException("No se encontró un carrito para el cliente con ID: " + clientId);
            }
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + clientId);
        }
    }
    // Método para vaciar un carrito
    public String clearCart(Long cartId) {
        return cartsRepository.findById(cartId).map(cart -> {
            cart.getProducts().clear();
            cartsRepository.save(cart);
            return "Empty cart";
        }).orElse("Cart not found");
    }

}
