package com.example.cook.model;

import com.example.cook.entity.TablesEntity;
import com.example.cook.enums.CustomerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class CustomersModel {
    private int id;
    private String name;
    private String phone;
    private Integer tablesId;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private String message;
    private TablesModel tablesModel;
    private OrderModel orderModel;
    private OrderItemModel orderItemModel;
}
