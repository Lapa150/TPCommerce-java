package com.example.demo.services;

import com.example.demo.repositories.InvoicesRepository;
import com.example.demo.entities.Cart;
import com.example.demo.entities.Client;
import com.example.demo.entities.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoicesService {

    private final InvoicesRepository invoicesRepository;
    private final CartsService cartsService;
    private final ClientsService clientsService;

    @Autowired
    public InvoicesService(InvoicesRepository invoicesRepository, CartsService cartsService, ClientsService clientsService) {
        this.invoicesRepository = invoicesRepository;
        this.cartsService = cartsService;
        this.clientsService = clientsService;
    }


    // Obtener todas las facturas
    public List<Invoice> getAllInvoices() {
        return invoicesRepository.findAll();
    }

    // Obtener una factura por ID
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoicesRepository.findById(id);
    }

    // Eliminar una factura por ID
    public void deleteInvoice(Long id) {
        invoicesRepository.deleteById(id);
    }

    // Generar factura para un cliente
    public String generateInvoiceForClient(Long clientId) {
        Optional<Client> clientToPay = clientsService.getClientById(clientId);
        if (clientToPay.isPresent()) {
            Client client = clientToPay.get();
            Optional<Cart> CarrtToDeliver = cartsService.getCartForClient(client);

            if (CarrtToDeliver.isPresent()) {
                Cart clientCart = CarrtToDeliver.get();


                Invoice invoice = new Invoice();
                invoice.setClient(client);
                invoice.setInvoiceDate(new Date());
                invoice.setTotalAmount(clientCart.getTotal());

                clientCart.setDelivered(true);
                cartsService.saveCart(clientCart);

                invoicesRepository.save(invoice);

                return "Invoice created";
            } else {
                throw new RuntimeException("Cart not found with Id: " + clientId);
            }
        } else {
            throw new RuntimeException("Client not found with Id: " + clientId);
        }
    }

}

