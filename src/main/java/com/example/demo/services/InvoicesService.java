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

    // Guardar o actualizar una factura
    public Invoice saveInvoice(Invoice invoice) {
        return invoicesRepository.save(invoice);
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
    public Invoice generateInvoiceForClient(Long clientId) {
        Optional<Client> clientOpt = clientsService.getClientById(clientId);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            Optional<Cart> clientCartOpt = cartsService.getCartForClient(client);

            if (clientCartOpt.isPresent()) {
                Cart clientCart = clientCartOpt.get();
                Invoice invoice = new Invoice();
                invoice.setClient(client);
                invoice.setInvoiceDate(new Date());
                invoice.setTotalAmount(clientCart.getTotal());

                return invoicesRepository.save(invoice);
            } else {
                throw new RuntimeException("Carrito no encontrado para el cliente con ID: " + clientId);
            }
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + clientId);
        }
    }
}
