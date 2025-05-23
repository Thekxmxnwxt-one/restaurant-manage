package com.example.cook.model;

import com.example.cook.enums.EmployeeRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EmployeesModel {
    private int id;
    private String name;
    private EmployeeRole role;
}
