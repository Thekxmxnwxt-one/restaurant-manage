package com.example.cook.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kitchen_stations")
@Data
public class KitchenStationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
