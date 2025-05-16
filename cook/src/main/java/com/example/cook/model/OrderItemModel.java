package com.example.cook.model;

import com.example.cook.entity.MenuItemEntity;
import com.example.cook.entity.OrdersEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemModel {
    private int id;
    private int orderId;
    private BigDecimal unitPrice;
    private int menuItemId;
    private Integer quantity;
    private String status;
    private String kitchenStation;
    private MenuItemModel menuItem;
}
