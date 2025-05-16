package com.example.cook.service;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.UserNativeRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserNativeRepository userNativeRepository;

    public UserService(UserNativeRepository userNativeRepository) {
        this.userNativeRepository = userNativeRepository;
    }

    public ResponseModel<UsersModel> getUserByUser(String username){
        ResponseModel<UsersModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            UsersModel transformData = this.userNativeRepository.findByUsername(username);
            if (transformData == null) {
                result.setStatus(404);
                result.setDescription("User not found");
            } else {
                result.setData(transformData);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<EmployeesModel> getEmployeeByUser(int employeeId){
        ResponseModel<EmployeesModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try{
            EmployeesModel employee = userNativeRepository.findEmployeeById(employeeId);

            if (employee != null) {
                result.setStatus(200);
                result.setDescription("success");
                result.setData(employee);
            } else {
                result.setStatus(404);
                result.setDescription("Employee not found");
            }
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
