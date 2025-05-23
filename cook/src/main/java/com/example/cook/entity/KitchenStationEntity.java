package com.example.cook.entity;

import com.example.cook.enums.KitchenstationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kitchen_stations")
@Data
public class KitchenStationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private KitchenstationType name;
}
