package com.example.cook.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EmployeesModel {
    private int id;
    private String name;
    private String role;
}
