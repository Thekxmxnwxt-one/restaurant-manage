package com.example.cook;

import com.example.cook.enums.EmployeeRole;
import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.EmployeesModel;
import com.example.cook.repository.EmployeeRepository;
import com.example.cook.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void test_get_all_employee_expect_success() {
        when(employeeRepository.findAllEmployee()).thenReturn(List.of(new EmployeesModel()));

        List<EmployeesModel> result = employeeService.getAllEmployee();

        Assertions.assertTrue(result.size()>0);
        verify(employeeRepository).findAllEmployee();
    }

    @Test
    public void test_get_employee_by_id_expect_success() {

        EmployeesModel mockEmp = new EmployeesModel();
        mockEmp.setId(1);

        when(employeeRepository.findEmployeeById(1)).thenReturn(mockEmp);

        EmployeesModel result = employeeService.getEmployeeById(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        verify(employeeRepository).findEmployeeById(1);
    }

    @Test
    public void test_get_employee_by_id_expect_not_found() {

        when(employeeRepository.findEmployeeById(1)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> employeeService.getEmployeeById(1));

        Assertions.assertTrue(ex.getMessage().contains("Employee with ID " + 1 + " not found."));
        verify(employeeRepository).findEmployeeById(1);
    }

    @Test
    public void test_insert_employee_expect_success() {

        EmployeesModel x = new EmployeesModel();
        x.setName("aa");
        x.setRole(EmployeeRole.chef);

        when(employeeRepository.insertEmployee(x)).thenReturn(1);

        int result = employeeService.insertEmployee(x);

        Assertions.assertEquals(1, result);
        verify(employeeRepository).insertEmployee(x);
    }

    @Test
    public void test_insert_employee_except_bad_request() {
        EmployeesModel x = new EmployeesModel();
        x.setName("aa");
        x.setRole(EmployeeRole.chef);

        when(employeeRepository.insertEmployee(x)).thenReturn(0);

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            employeeService.insertEmployee(x);
        });

        Assertions.assertTrue(ex.getMessage().contains("Insert failed for employee:"));
        verify(employeeRepository).insertEmployee(x);
    }

    @Test
    public void test_update_employee_expect_success(){

        EmployeesModel x = new EmployeesModel();
        x.setName("aa");
        x.setRole(EmployeeRole.chef);

        EmployeesModel y = new EmployeesModel();
        y.setName("bb");
        y.setRole(EmployeeRole.chef);

        when(employeeRepository.updateEmployee(x)).thenReturn(y);

        EmployeesModel result = employeeService.updateEmployee(x);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(x.getId(), result.getId());
        verify(employeeRepository).updateEmployee(x);
    }

    @Test
    public void test_update_employee_expect_not_found(){

        EmployeesModel y = new EmployeesModel();
        y.setName("bb");
        y.setRole(EmployeeRole.chef);

        when(employeeRepository.updateEmployee(y)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> employeeService.updateEmployee(y));

        Assertions.assertTrue(ex.getMessage().contains("Update failed: Employee not found or no changes made."));
        verify(employeeRepository).updateEmployee(y);
    }

    @Test
    public void test_delete_employee_success() {
        when(employeeRepository.deleteEmployee(1)).thenReturn("Delete success");

        String result = employeeService.deleteEmployee(1);

        Assertions.assertEquals("Delete success", result);
        verify(employeeRepository).deleteEmployee(1);
    }

    @Test
    public void test_delete_employee_not_found() {
        when(employeeRepository.deleteEmployee(1)).thenReturn("Not found");

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () ->
                employeeService.deleteEmployee(1));

        Assertions.assertTrue(ex.getMessage().contains("Employee not found"));
    }

    @Test
    public void test_delete_employee_referenced_in_orders() {
        when(employeeRepository.deleteEmployee(1)).thenThrow(new DataIntegrityViolationException("FK error"));

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () ->
                employeeService.deleteEmployee(1));

        Assertions.assertTrue(ex.getMessage().contains("still referenced in orders"));
    }

}
