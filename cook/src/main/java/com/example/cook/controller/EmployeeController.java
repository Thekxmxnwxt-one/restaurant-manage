package com.example.cook.controller;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.KitchenLogModel;
import com.example.cook.model.MenuItemModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public ResponseModel<List<EmployeesModel>> getAllEmployee(){
        List<EmployeesModel> emp = employeeService.getAllEmployee();
        ResponseModel<List<EmployeesModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(emp);
        return response;
    }

    @GetMapping("/employee/{id}")
    public ResponseModel<EmployeesModel> getEmployeeById(@PathVariable int id){
        EmployeesModel emp = employeeService.getEmployeeById(id);
        ResponseModel<EmployeesModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(emp);
        return response;
    }

    @PostMapping("/manager/employee")
    public ResponseModel<Integer> insertEmployee(@RequestBody EmployeesModel employeesModel){
        int emp = employeeService.insertEmployee(employeesModel);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(emp);
        return response;
    }

    @PutMapping("/manager/employee")
    public ResponseModel<EmployeesModel> updateEmployee(@RequestBody EmployeesModel employeesModel) {
        EmployeesModel emp = employeeService.updateEmployee(employeesModel);
        ResponseModel<EmployeesModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(emp);
        return response;
    }

    @DeleteMapping("/manager/employee/{id}")
    public ResponseModel<String> deleteEmployee(@PathVariable int id){
        String emp = employeeService.deleteEmployee(id);
        ResponseModel<String> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(emp);
        return response;
    }
}
