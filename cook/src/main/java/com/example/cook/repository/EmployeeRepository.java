package com.example.cook.repository;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.KitchenLogModel;

import java.util.List;

public interface EmployeeRepository {
    public List<EmployeesModel> findAllEmployee();
    public EmployeesModel findEmployeeById(int id);
    public int insertEmployee(EmployeesModel employeesModel);
    public EmployeesModel updateEmployee(EmployeesModel employeesModel);
    public String deleteEmployee(int id);
}
