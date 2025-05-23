package com.example.cook.entity;

import com.example.cook.enums.MenuCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@Data
public class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    private Boolean available;
}
