package com.example.cook.service;

import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.CustomersModel;
import com.example.cook.repository.CustomerNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerNativeRepository customerNativeRepository;

    public CustomerService(CustomerNativeRepository customerNativeRepository) {
        this.customerNativeRepository = customerNativeRepository;
    }

    public List<CustomersModel> getAllCustomer() {
        return customerNativeRepository.findAllCustomer();
    }

    public List<CustomersModel> getAllActiveCustomer() {
        return customerNativeRepository.findAllActiveCustomer();
    }

    public CustomersModel getCustomerByCustomerId(int customerId) {
        CustomersModel cs = customerNativeRepository.findCustomerByCustomerId(customerId);
        if (cs == null) {
            throw new NotFoundException("Customer with ID " + customerId + " not found.");
        }
        return cs;
    }

    public int insertCustomer(CustomersModel customersModel) {
        int insertedRows = customerNativeRepository.insertCustomer(customersModel);
        if (insertedRows == 0) {
            throw new BadRequestException("Insert failed for customer.");
        }
        return insertedRows;
    }

    public CustomersModel updateCustomerStatus(CustomersModel customersModel) {
        CustomersModel updated = customerNativeRepository.updateStatusCustomer(customersModel);
        if (updated == null) {
            throw new NotFoundException("Customer not found or update failed.");
        }
        return updated;
    }
}
