package com.example.cook.model;

import com.example.cook.enums.TableStatus;
import lombok.Data;

@Data
public class TablesModel {
    private int id;
    private int tableNumber;
    private TableStatus status;
    private CustomersModel customer;
}
