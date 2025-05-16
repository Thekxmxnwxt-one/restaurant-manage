package com.example.cook.controller;

import com.example.cook.model.CustomersModel;
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
    public ResponseModel<List<CustomersModel>> getAllTable(){
        return this.customerService.getAllCustomer();
    }

    @GetMapping("/customers/active")
    public ResponseModel<List<CustomersModel>> getAllActiveTable(){
        return this.customerService.getAllActiveCustomer();
    }

    @GetMapping("/customers/{customerId}")
    public ResponseModel<CustomersModel> getTableByTableNumber(@PathVariable int customerId){
        return this.customerService.getCustomerByCustomerId(customerId);
    }

    @PostMapping("/customers")
    public ResponseModel<Integer> insertCustomer(@RequestBody CustomersModel customersModel){
        return this.customerService.insertCustomer(customersModel);
    }

    @PutMapping("/customers/status")
    public ResponseModel<CustomersModel> updateTable(@RequestBody CustomersModel customersModel) {
        return this.customerService.updateCustomerStatus(customersModel);
    }
}
