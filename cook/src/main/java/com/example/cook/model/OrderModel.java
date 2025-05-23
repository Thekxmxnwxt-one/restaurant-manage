package com.example.cook.model;

import com.example.cook.entity.CustomersEntity;
import com.example.cook.entity.EmployeesEntity;
import com.example.cook.entity.TablesEntity;
import com.example.cook.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderModel {
    private int id;
    private int customerId;
    private int tableId;
    private int employeeId;
    private LocalDateTime orderedAt;
    private OrderStatus status;
    private LocalDateTime closedAt;
    private CustomersModel customer;
    private TablesModel table;
    private EmployeesModel employee;
    private List<OrderItemModel> items;
}
