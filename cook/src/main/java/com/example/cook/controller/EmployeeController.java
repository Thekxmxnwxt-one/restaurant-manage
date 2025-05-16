package com.example.cook.controller;

import com.example.cook.model.EmployeesModel;
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
        return this.employeeService.getAllEmployee();
    }

    @GetMapping("/employee/{id}")
    public ResponseModel<EmployeesModel> getEmployeeById(@PathVariable int id){
        return this.employeeService.getEmployeeById(id);
    }

    @PostMapping("/employee")
    public ResponseModel<Integer> insertEmployee(@RequestBody EmployeesModel employeesModel){
        return this.employeeService.insertEmployee(employeesModel);
    }

    @PutMapping("/employee")
    public ResponseModel<EmployeesModel> updateEmployee(@RequestBody EmployeesModel employeesModel) {
        return this.employeeService.updateEmployee(employeesModel);
    }

    @DeleteMapping("employee/{id}")
    public ResponseModel<String> deleteEmployee(@PathVariable int id){
        return this.employeeService.deleteEmployee(id);
    }
}
