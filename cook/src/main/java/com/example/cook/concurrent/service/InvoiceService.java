package com.example.cook.concurrent.service;

import com.example.cook.concurrent.model.InvoiceModel;
import com.example.cook.concurrent.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void saveInvoicesParallel(List<InvoiceModel> invoices) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

        invoices.parallelStream().forEach(invoice -> {
            try {
                log("Saving " + invoice.getInvoiceNumber());
                invoiceRepository.save(invoice);
                log("✔ Saved " + invoice.getInvoiceNumber());
            } catch (Exception e) {
                log("❌ Failed to save " + invoice.getInvoiceNumber() + ": " + e.getMessage());
            }
        });
    }

    private void log(String msg) {
        System.out.printf("[%s][%s] %s%n",
                java.time.LocalDateTime.now(),
                Thread.currentThread().getName(),
                msg);
    }
}
