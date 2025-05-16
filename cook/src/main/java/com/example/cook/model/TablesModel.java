package com.example.cook.model;

import lombok.Data;

@Data
public class TablesModel {
    private int id;
    private int tableNumber;
    private String status;
    private CustomersModel customer;
}
