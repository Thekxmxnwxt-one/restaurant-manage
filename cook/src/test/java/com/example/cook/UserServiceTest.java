package com.example.cook;

import com.example.cook.exception.AlreadyExistsException;
import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.EmployeesModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.UserNativeRepository;
import com.example.cook.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserNativeRepository userNativeRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void test_get_user_by_user_expect_success() {

        String username = "john";
        UsersModel mockUser = new UsersModel();
        mockUser.setUsername(username);

        when(userNativeRepository.findByUsername(username)).thenReturn(mockUser);

        UsersModel result = userService.getUserByUser(username);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result.getUsername());
        verify(userNativeRepository).findByUsername(username);
    }

    @Test
    public void test_get_user_by_user_expect_not_found() {

        String username = "nonexistent";
        when(userNativeRepository.findByUsername(username)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserByUser(username));
        verify(userNativeRepository).findByUsername(username);
    }

    @Test
    public void test_insert_employee_with_user_expect_success() {

        EmployeesModel employeesModel = new EmployeesModel();
        String username = "newUser";
        String password = "pass";

        when(userNativeRepository.existsByUsername(username)).thenReturn(false);
        when(userNativeRepository.insertEmployeeWithUser(employeesModel, username, password)).thenReturn(1);

        int result = userService.insertEmployeeWithUser(employeesModel, username, password);

        Assertions.assertEquals(1, result);

        verify(userNativeRepository).existsByUsername(username);
        verify(userNativeRepository).insertEmployeeWithUser(employeesModel, username, password);
    }

    @Test
    public void test_insert_employee_with_user_expect_already_exist() {

        EmployeesModel employeesModel = new EmployeesModel();
        String username = "existingUser";
        String password = "pass";

        // Mock ว่าผู้ใช้มีอยู่แล้ว
        when(userNativeRepository.existsByUsername(username)).thenReturn(true);

        // ตรวจสอบว่าโยน exception
        Assertions.assertThrows(AlreadyExistsException.class,
                () -> userService.insertEmployeeWithUser(employeesModel, username, password));

        verify(userNativeRepository).existsByUsername(username);
        verify(userNativeRepository, never()).insertEmployeeWithUser(any(), anyString(), anyString());
    }

    @Test
    public void test_insert_employee_with_user_expect_bad_request() {

        EmployeesModel employeesModel = new EmployeesModel();
        String username = "newUser";
        String password = "pass";

        when(userNativeRepository.existsByUsername(username)).thenReturn(false);
        when(userNativeRepository.insertEmployeeWithUser(employeesModel, username, password)).thenReturn(0);

        Assertions.assertThrows(BadRequestException.class,
                () -> userService.insertEmployeeWithUser(employeesModel, username, password));

        verify(userNativeRepository).existsByUsername(username);
        verify(userNativeRepository).insertEmployeeWithUser(employeesModel, username, password);
    }
}
