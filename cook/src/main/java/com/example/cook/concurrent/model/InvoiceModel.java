package com.example.cook.concurrent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceModel {
    private Integer id;
    private String invoiceNumber;
}
