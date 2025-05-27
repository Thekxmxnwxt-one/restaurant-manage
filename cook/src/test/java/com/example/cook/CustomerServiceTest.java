package com.example.cook;

import com.example.cook.enums.CustomerStatus;
import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.CustomersModel;
import com.example.cook.repository.CustomerNativeRepository;
import com.example.cook.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerNativeRepository customerNativeRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void test_get_all_customer_expect_success() {
        when(customerNativeRepository.findAllCustomer()).thenReturn(List.of(new CustomersModel()));

        List<CustomersModel> result = customerService.getAllCustomer();

        Assertions.assertTrue(result.size()>0);
        verify(customerNativeRepository).findAllCustomer();
    }

    @Test
    public void test_get_all_customer_active_expect_success() {
        when(customerNativeRepository.findAllActiveCustomer()).thenReturn(List.of(new CustomersModel()));

        List<CustomersModel> result = customerService.getAllActiveCustomer();

        Assertions.assertTrue(result.size()>0);
        verify(customerNativeRepository).findAllActiveCustomer();
    }

    @Test
    public void test_get_customer_by_id_expect_success() {

        CustomersModel mockCs = new CustomersModel();
        mockCs.setId(1);

        when(customerNativeRepository.findCustomerByCustomerId(1)).thenReturn(mockCs);

        CustomersModel result = customerService.getCustomerByCustomerId(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        verify(customerNativeRepository).findCustomerByCustomerId(1);
    }

    @Test
    public void test_get_customer_by_id_expect_not_found() {

        when(customerNativeRepository.findCustomerByCustomerId(1)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> customerService.getCustomerByCustomerId(1));

        Assertions.assertTrue(ex.getMessage().contains("Customer with ID " + 1 + " not found."));
        verify(customerNativeRepository).findCustomerByCustomerId(1);
    }

    @Test
    public void test_insert_employee_expect_success() {

        CustomersModel customersModel = new CustomersModel();
        customersModel.setName("aa");
        customersModel.setPhone("088888888");
        customersModel.setTablesId(1);
        customersModel.setStatus(CustomerStatus.seated);

        when(customerNativeRepository.insertCustomer(customersModel)).thenReturn(1);

        int result = customerService.insertCustomer(customersModel);

        Assertions.assertEquals(1, result);
        verify(customerNativeRepository).insertCustomer(customersModel);
    }

    @Test
    public void test_insert_employee_except_bad_request() {
        CustomersModel customersModel = new CustomersModel();
        customersModel.setName("aa");
        customersModel.setPhone("088888888");
        customersModel.setTablesId(1);
        customersModel.setStatus(CustomerStatus.seated);

        when(customerNativeRepository.insertCustomer(customersModel)).thenReturn(0);

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            customerService.insertCustomer(customersModel);
        });

        Assertions.assertTrue(ex.getMessage().contains("Insert failed for customer."));
        verify(customerNativeRepository).insertCustomer(customersModel);
    }

    @Test
    public void test_update_employee_expect_success(){

        CustomersModel oldCustomersModel = new CustomersModel();
        oldCustomersModel.setName("aa");
        oldCustomersModel.setPhone("088888888");
        oldCustomersModel.setTablesId(1);
        oldCustomersModel.setStatus(CustomerStatus.seated);

        CustomersModel customersModel = new CustomersModel();
        customersModel.setName("aa");
        customersModel.setPhone("088888888");
        customersModel.setTablesId(1);
        customersModel.setStatus(CustomerStatus.done);

        when(customerNativeRepository.updateStatusCustomer(oldCustomersModel)).thenReturn(customersModel);

        CustomersModel result = customerService.updateCustomerStatus(oldCustomersModel);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(oldCustomersModel.getId(), result.getId());
        verify(customerNativeRepository).updateStatusCustomer(oldCustomersModel);
    }

    @Test
    public void test_update_employee_expect_not_found(){

        CustomersModel customersModel = new CustomersModel();
        customersModel.setName("aa");
        customersModel.setPhone("088888888");
        customersModel.setTablesId(1);
        customersModel.setStatus(CustomerStatus.done);

        when(customerNativeRepository.updateStatusCustomer(customersModel)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> customerService.updateCustomerStatus(customersModel));

        Assertions.assertTrue(ex.getMessage().contains("Customer not found or update failed."));
        verify(customerNativeRepository).updateStatusCustomer(customersModel);
    }
}
