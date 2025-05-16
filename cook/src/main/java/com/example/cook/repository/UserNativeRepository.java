package com.example.cook.repository;

import com.example.cook.entity.UsersEntity;
import com.example.cook.model.EmployeesModel;
import com.example.cook.model.UsersModel;

public interface UserNativeRepository {
    public UsersModel findByUsername(String username);
    public EmployeesModel findEmployeeById(int employeeId);
}
