package com.example.cook.model;

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