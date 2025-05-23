package com.example.cook.entity;

import com.example.cook.enums.LogStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kitchen_logs")
@Data
public class KitchenLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItemsEntity orderItemId;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private KitchenStationEntity stationId;

    @Enumerated(EnumType.STRING)
    private LogStatus status;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;
}
