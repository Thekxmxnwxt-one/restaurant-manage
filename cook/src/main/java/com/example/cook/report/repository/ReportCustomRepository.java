package com.example.cook.report.repository;

import com.example.cook.model.OrderModel;

import java.sql.Connection;

public interface ReportCustomRepository {

    public Connection getConnection();

    public OrderModel findOrderByOrderId(int orderId);

    public OrderModel findOrderById(int orderId);
}