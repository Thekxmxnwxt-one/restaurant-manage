package com.example.cook.concurrent.repository;

import com.example.cook.concurrent.model.InvoiceModel;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepository {
    public void save(InvoiceModel invoice) throws InterruptedException {
        // จำลองการ save แบบช้า ๆ (หรือใช้ JPA จริงก็ได้)
        Thread.sleep(200);
    }
}
