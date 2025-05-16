package com.example.cook.model;

import com.example.cook.entity.KitchenStationEntity;
import com.example.cook.entity.OrderItemsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class KitchenLogModel {
    private int id;
    private int orderItemId;
    private int stationId;
    private String status;
    private LocalDateTime timestamp;
}
