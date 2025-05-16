package com.example.cook.controller;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.UsersModel;
import com.example.cook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseModel<UsersModel> getUser(@RequestBody UsersModel username) {
        return this.userService.getUserByUser(username.getUsername());
    }

    @GetMapping("/me")
    public ResponseModel<EmployeesModel> getEmployee(@RequestParam int employeeId) {
        return this.userService.getEmployeeByUser(employeeId);
    }


}
