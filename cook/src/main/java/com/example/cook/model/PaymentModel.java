package com.example.cook.model;

import com.example.cook.entity.OrdersEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentModel {
    private int id;
    private int orderId;
    private BigDecimal amount;
    private String method;
    private LocalDateTime paidAt;
    private OrderModel order;

}
