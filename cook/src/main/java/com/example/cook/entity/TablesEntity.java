package com.example.cook.entity;

import com.example.cook.enums.TableStatus;
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

    @Enumerated(EnumType.STRING)
    private TableStatus status;
}
