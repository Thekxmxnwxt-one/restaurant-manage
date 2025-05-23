package com.example.cook.service;

import com.example.cook.exception.AlreadyExistsException;
import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.EmployeesModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.UserNativeRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserNativeRepository userNativeRepository;

    public UserService(UserNativeRepository userNativeRepository) {
        this.userNativeRepository = userNativeRepository;
    }

    public UsersModel getUserByUser(String username) {
        UsersModel user = userNativeRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public EmployeesModel getEmployeeByUser(int employeeId) {
        EmployeesModel employee = userNativeRepository.findEmployeeById(employeeId);
        if (employee == null) {
            throw new NotFoundException("Employee not found");
        }
        return employee;
    }

    public int insertEmployeeWithUser(EmployeesModel employeesModel, String username, String rawPassword) {

        boolean exists = userNativeRepository.existsByUsername(username);
        if (exists) {
            throw new AlreadyExistsException("User '" + username + "' already exists");
        }

        int rowsInserted = userNativeRepository.insertEmployeeWithUser(employeesModel, username, rawPassword);

        if (rowsInserted <= 0) {
            throw new BadRequestException("Insert failed");
        }

        return rowsInserted;
    }
}
