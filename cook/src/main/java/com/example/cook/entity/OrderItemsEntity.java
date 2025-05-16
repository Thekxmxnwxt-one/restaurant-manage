package com.example.cook.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
public class OrderItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrdersEntity orderId;

    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItemId;

    private Integer quantity;
    private String status;
    private String kitchenStation;
}
