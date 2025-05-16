package com.example.cook.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomersEntity customerId;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TablesEntity tableId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeesEntity employeeId;

    @CreationTimestamp
    @Column(name = "ordered_at", updatable = false)
    private LocalDateTime orderedAt;

    private String status;

    @CreationTimestamp
    @Column(name = "closed_at", updatable = false)
    private LocalDateTime closedAt;
}
