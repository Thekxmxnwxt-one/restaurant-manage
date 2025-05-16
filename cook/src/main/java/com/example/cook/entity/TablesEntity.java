package com.example.cook.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Entity
@Data
@Table(name = "tables")
public class TablesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int tableNumber;
    private String status;
}
