package com.example.cook.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemModel {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private String category;
    private Boolean available;
}
