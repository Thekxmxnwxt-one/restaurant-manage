package com.example.cook.concurrent.controller;

import com.example.cook.concurrent.model.InvoiceModel;
import com.example.cook.concurrent.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/save-parallel")
    public ResponseEntity<String> saveInvoices() {
        List<InvoiceModel> list = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new InvoiceModel(i, "INV-" + i))
                .toList();

        invoiceService.saveInvoicesParallel(list);
        return ResponseEntity.ok("All invoices submitted for save.");
    }
}
