package com.example.cook.controller;

import com.example.cook.exception.BadRequestException;
import com.example.cook.model.*;
import com.example.cook.security.JwtUtil;
import com.example.cook.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private UserService userService;
    private JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseModel<Map<String, Object>> login(@RequestBody UsersModel usersModel) {
        UsersModel user = userService.getUserByUser(usersModel.getUsername());

        if (user == null || !passwordEncoder.matches(usersModel.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getEmployeesModel().getRole().name());

        UserResponseModel userResponse = new UserResponseModel(user.getId(), user.getUsername(), user.getEmployeesModel());

        Map<String, Object> data = Map.of(
                "token", token,
                "user", userResponse
        );

        ResponseModel<Map<String, Object>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(data);
        return response;
    }

    @GetMapping("/me")
    public EmployeesModel getEmployee(@RequestParam int employeeId) {
        return this.userService.getEmployeeByUser(employeeId);
    }

    @PostMapping("/register")
    public ResponseModel<Integer> register(@RequestBody RegisterRequestModel request) {
        EmployeesModel employee = new EmployeesModel();
        employee.setName(request.getName());
        employee.setRole(request.getRole());

        int inserted = this.userService.insertEmployeeWithUser(employee, request.getUsername(), request.getPassword());

        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("Register successful");
        response.setData(inserted);
        return response;
    }
}
