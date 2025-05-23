package com.example.cook.model;

import com.example.cook.entity.OrdersEntity;
import com.example.cook.enums.PaymentMethod;
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
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private OrderModel order;

}
