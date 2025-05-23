package com.example.cook.model;

import com.example.cook.enums.EmployeeRole;
import lombok.Data;

@Data
public class RegisterRequestModel {
    private String name;
    private EmployeeRole role;
    private String username;
    private String password;
}
