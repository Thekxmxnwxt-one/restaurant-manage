package com.example.cook.report;

import com.example.cook.model.*;
import com.example.cook.report.repository.ReportCustomRepository;
import com.example.cook.report.service.GeneratorReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfReportTest {

    @Mock
    private ReportCustomRepository reportCustomRepository;

    @InjectMocks
    private GeneratorReportService generatorReportService;

    @Test
    public void test_get_pdf_expect_success() throws Exception {
        int orderId = 123;

        OrderModel mockOrder = createMockOrder(orderId);

        when(reportCustomRepository.findOrderById(orderId)).thenReturn(mockOrder);

    }

    private OrderModel createMockOrder(int orderId) {
        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setOrderedAt(LocalDateTime.now());

        CustomersModel customer = new CustomersModel();
        customer.setId(1);
        customer.setName("John Doe");
        customer.setPhone("0123456789");
        order.setCustomer(customer);

        TablesModel table = new TablesModel();
        table.setId(10);
        table.setTableNumber(5);
        order.setTable(table);

        EmployeesModel employee = new EmployeesModel();
        employee.setId(100);
        employee.setName("Alice");
        order.setEmployee(employee);

        List<OrderItemModel> items = new ArrayList<>();
        OrderItemModel item1 = new OrderItemModel();
        item1.setId(1000);
        item1.setUnitPrice(new BigDecimal("99.99"));
        item1.setMenuItemId(2000);
        item1.setQuantity(2);

        MenuItemModel menu1 = new MenuItemModel();
        menu1.setId(2000);
        menu1.setName("Spaghetti");
        item1.setMenuItem(menu1);

        items.add(item1);

        order.setItems(items);

        return order;
    }
}


