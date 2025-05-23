package com.example.cook.service;

import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.EmployeesModel;
import com.example.cook.repository.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeesModel> getAllEmployee() {
        return employeeRepository.findAllEmployee();
    }

    public EmployeesModel getEmployeeById(int id) {
        EmployeesModel emp = employeeRepository.findEmployeeById(id);
        if (emp == null) {
            throw new NotFoundException("Employee with ID " + id + " not found.");
        }
        return emp;
    }

    public int insertEmployee(EmployeesModel employeesModel) {
        int insertedRows = employeeRepository.insertEmployee(employeesModel);
        if (insertedRows == 0) {
            throw new BadRequestException("Insert failed for employee: " + employeesModel);
        }
        return insertedRows;
    }

    public EmployeesModel updateEmployee(EmployeesModel employeesModel) {
        EmployeesModel updated = employeeRepository.updateEmployee(employeesModel);
        if (updated == null) {
            throw new NotFoundException("Update failed: Employee not found or no changes made.");
        }
        return updated;
    }

    public String deleteEmployee(int id) {
        try {
            String message = employeeRepository.deleteEmployee(id);

            if ("Delete success".equals(message)) {
                return message;
            } else {
                throw new NotFoundException("Employee not found or already deleted.");
            }

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Cannot delete employee: This employee is still referenced in orders.");
        }
    }
}
