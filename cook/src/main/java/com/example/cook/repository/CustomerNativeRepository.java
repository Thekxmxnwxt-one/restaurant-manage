package com.example.cook.repository;

import com.example.cook.model.CustomersModel;
import com.example.cook.model.TablesModel;

import java.util.List;

public interface CustomerNativeRepository {
    public List<CustomersModel> findAllCustomer();
    public List<CustomersModel> findAllActiveCustomer();
    public CustomersModel findCustomerByCustomerId(int customerId);
    public int insertCustomer(CustomersModel customersModel);
    public CustomersModel updateStatusCustomer(CustomersModel customersModel);

}
