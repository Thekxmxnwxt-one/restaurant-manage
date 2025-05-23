package com.example.cook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersModel {
    private int id;
    private String username;

    private String password;
    
    private int employeeId;
    private LocalDateTime createdAt;

    private EmployeesModel employeesModel;
}