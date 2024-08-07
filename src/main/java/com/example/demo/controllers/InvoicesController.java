package com.example.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.example.demo.entities.Invoice;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.services.InvoicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "api/v1/invoices")
public class InvoicesController {

    @Autowired
    private InvoicesService invoicesService;


    @PostMapping("/generate-invoice/{clientId}")
    @Operation(summary = "Generate invoice for client",
            description = "Generates an invoice for a client through its id and set the cart to delivered" )
    public ResponseEntity<String> generateInvoiceForClient(@PathVariable Long clientId) {
        try {
            String resultMessage = invoicesService.generateInvoiceForClient(clientId);
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error generating invoice: " + e.getMessage());
            return new ResponseEntity<>("Error generating invoice", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    @Operation(summary = "Get all the invoices",
            description = "Shows all the invoices with their data" )
    public List<Invoice> getAllInvoices() {
        return invoicesService.getAllInvoices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by id",
            description = "shows the invoice data placing its id" )
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoicesService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete invoice",
            description = "delete an invoice placing its id" )
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        try {
            invoicesService.deleteInvoice(id);
            return ResponseEntity.ok("Invoice deleted");
        } catch (Exception exception) {
            System.err.println("Error deleting invoice: " + exception.getMessage());
            return ResponseEntity.status(500).body("DELETE ERROR");
        }
    }






}
