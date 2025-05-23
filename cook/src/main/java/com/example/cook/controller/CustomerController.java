package com.example.cook.controller;

import com.example.cook.model.CustomersModel;
import com.example.cook.model.EmployeesModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.TablesModel;
import com.example.cook.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseModel<List<CustomersModel>> getAllCustomer(){
        List<CustomersModel> cs = customerService.getAllCustomer();
        ResponseModel<List<CustomersModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(cs);
        return response;
    }

    @GetMapping("/customers/active")
    public ResponseModel<List<CustomersModel>> getAllActiveTable(){
        List<CustomersModel> cs = customerService.getAllCustomer();
        ResponseModel<List<CustomersModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(cs);
        return response;
    }

    @GetMapping("/customers/{customerId}")
    public ResponseModel<CustomersModel> getCustomerById(@PathVariable int customerId){
        CustomersModel cs = customerService.getCustomerByCustomerId(customerId);
        ResponseModel<CustomersModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(cs);
        return response;
    }

    @PostMapping("/customers")
    public ResponseModel<Integer> insertCustomer(@RequestBody CustomersModel customersModel){
        int cs = customerService.insertCustomer(customersModel);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(cs);
        return response;
    }

    @PutMapping("/customers/status")
    public ResponseModel<CustomersModel> updateCustomer(@RequestBody CustomersModel customersModel) {
        CustomersModel cs = customerService.updateCustomerStatus(customersModel);
        ResponseModel<CustomersModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(cs);
        return response;
    }
}
