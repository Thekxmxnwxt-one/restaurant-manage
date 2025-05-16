package com.example.cook.service;

import com.example.cook.model.CustomersModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.TablesModel;
import com.example.cook.repository.CustomerNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerNativeRepository customerNativeRepository;

    public CustomerService(CustomerNativeRepository customerNativeRepository) {
        this.customerNativeRepository = customerNativeRepository;
    }

    public ResponseModel<List<CustomersModel>> getAllCustomer(){
        ResponseModel<List<CustomersModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<CustomersModel> tables = this.customerNativeRepository.findAllCustomer();
            result.setData(tables);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<List<CustomersModel>> getAllActiveCustomer(){
        ResponseModel<List<CustomersModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<CustomersModel> tables = this.customerNativeRepository.findAllActiveCustomer();
            result.setData(tables);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<CustomersModel> getCustomerByCustomerId(int customerId){
        ResponseModel<CustomersModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            CustomersModel cs = this.customerNativeRepository.findCustomerByCustomerId(customerId);
            result.setData(cs);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertCustomer(CustomersModel customersModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.customerNativeRepository.insertCustomer(customersModel);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<CustomersModel> updateCustomerStatus(CustomersModel customersModel){
        ResponseModel<CustomersModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            CustomersModel updated = this.customerNativeRepository.updateStatusCustomer(customersModel);

            if (updated != null) {
                result.setStatus(200);
                result.setDescription("Update customer status success");
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
}
