package com.example.demo.controllers;

import com.example.demo.entities.Invoice;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.services.InvoicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/invoices")
public class InvoicesController {

    @Autowired
    private InvoicesService invoicesService;


    //Generar factura para cliente
    @PostMapping("/generate/{clientId}")
    public ResponseEntity<Invoice> generateInvoiceForClient(@PathVariable Long clientId) {
        try {
            Invoice generatedInvoice = invoicesService.generateInvoiceForClient(clientId);
            return ResponseEntity.ok(generatedInvoice);
        } catch (Exception e) {
            System.err.println("Error al generar factura: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }



    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoicesService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoicesService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        try {
            invoicesService.deleteInvoice(id);
            return ResponseEntity.ok("Factura eliminada con éxito");
        } catch (Exception exception) {
            System.err.println("Error borrando factura: " + exception.getMessage());
            return ResponseEntity.status(500).body("DELETE ERROR");
        }
    }






}
