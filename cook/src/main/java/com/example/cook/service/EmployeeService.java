package com.example.cook.service;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.MenuItemModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.repository.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ResponseModel<List<EmployeesModel>> getAllEmployee(){
        ResponseModel<List<EmployeesModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<EmployeesModel> emp = this.employeeRepository.findAllEmployee();
            result.setData(emp);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<EmployeesModel> getEmployeeById(int id){
        ResponseModel<EmployeesModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            EmployeesModel emp = this.employeeRepository.findEmployeeById(id);
            result.setData(emp);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertEmployee(EmployeesModel employeesModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.employeeRepository.insertEmployee(employeesModel);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<EmployeesModel> updateEmployee(EmployeesModel employeesModel){
        ResponseModel<EmployeesModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            EmployeesModel updated = this.employeeRepository.updateEmployee(employeesModel);

            if (updated != null) {
                result.setStatus(200);
                result.setDescription("Update table status success");
                result.setData(updated);
            } else {
                result.setStatus(404);
                result.setDescription("Table not found or update failed");
                result.setData(null);
            }
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<String> deleteEmployee(int id) {
        ResponseModel<String> result = new ResponseModel<>();

        try {
            String message = this.employeeRepository.deleteEmployee(id);

            if ("Delete success".equals(message)) {
                result.setStatus(200);
                result.setDescription("Employee deleted successfully");
                result.setData(message);
            } else {
                result.setStatus(404);
                result.setDescription("Employee not found or already deleted");
                result.setData(message);
            }

        } catch (DataIntegrityViolationException e) {
            result.setStatus(400);
            result.setDescription("Cannot delete employee: This employee is still referenced in orders");
            result.setData(null);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription("Internal server error: " + e.getMessage());
            result.setData(null);
        }

        return result;
    }

}
