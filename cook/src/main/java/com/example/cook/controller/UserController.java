package com.example.cook.controller;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.UsersModel;
import com.example.cook.security.JwtUtil;
import com.example.cook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private UserService userService;
    private JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseModel<Map<String, Object>> login(@RequestBody UsersModel usersModel) {
        ResponseModel<Map<String, Object>> response = new ResponseModel<>();

        UsersModel user = userService.getUserByUser(usersModel.getUsername()).getData();

        if (user == null || !user.getPassword().equals(usersModel.getPassword())) {
            response.setStatus(401);
            response.setDescription("Invalid username or password");
            return response;
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getEmployeesModel().getRole());

        Map<String, Object> data = Map.of(
                "token", token,
                "user", user
        );

        response.setStatus(200);
        response.setDescription("success");
        response.setData(data);

        return response;
    }


    @GetMapping("/me")
    public ResponseModel<EmployeesModel> getEmployee(@RequestParam int employeeId) {
        return this.userService.getEmployeeByUser(employeeId);
    }


}
