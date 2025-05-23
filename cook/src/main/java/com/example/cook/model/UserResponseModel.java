package com.example.cook.model;

import lombok.Data;

@Data
public class UserResponseModel {
    private int id;
    private String username;
    private EmployeesModel employeesModel;

    public UserResponseModel(int id, String username, EmployeesModel employeesModel) {
        this.id = id;
        this.username = username;
        this.employeesModel = employeesModel;
    }

}
