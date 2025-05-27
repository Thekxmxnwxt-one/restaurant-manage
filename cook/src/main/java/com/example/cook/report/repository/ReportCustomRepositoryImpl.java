package com.example.cook.report.repository;

import com.example.cook.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@Repository
public class ReportCustomRepositoryImpl implements ReportCustomRepository{

    private JdbcTemplate jdbcTemplate;

    public ReportCustomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection =  this.jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    @Override
    public OrderModel findOrderByOrderId(int orderId) {
        String sql = """
        SELECT
           o.id as order_id,
           o.ordered_at,
           c.name as customer_name,
           c.phone as customer_phone,
           t.table_number,
           e.name as employee_name,
           i.unit_price,
           i.quantity,
           m.name as menu_name
        FROM orders o
        INNER JOIN customers c ON o.customer_id = c.id
        INNER JOIN tables t ON o.table_id = t.id
        INNER JOIN employees e ON o.employee_id = e.id
        INNER JOIN order_items i ON i.order_id = o.id
        INNER JOIN menu_items m ON i.menu_item_id = m.id
        WHERE o.id = ?
        ORDER BY m.name
    """;

        return jdbcTemplate.query(sql, new Object[]{orderId}, rs -> {
            OrderModel order = null;

            while (rs.next()) {
                if (order == null) {
                    order = new OrderModel();
                    order.setId(rs.getInt("order_id"));
                    order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());

                    CustomersModel customer = new CustomersModel();
                    customer.setId(rs.getInt("customer_id"));
                    customer.setName(rs.getString("customer_name"));
                    customer.setPhone(rs.getString("customer_phone"));
                    order.setCustomer(customer);

                    TablesModel table = new TablesModel();
                    table.setId(rs.getInt("table_id"));
                    table.setTableNumber(rs.getInt("table_number"));
                    order.setTable(table);

                    EmployeesModel employee = new EmployeesModel();
                    employee.setId(rs.getInt("employee_id"));
                    employee.setName(rs.getString("employee_name"));
                    order.setEmployee(employee);

                    order.setItems(new ArrayList<>());
                }

                int itemId = rs.getInt("order_item_id");
                if (itemId > 0) {
                    OrderItemModel item = new OrderItemModel();
                    item.setId(itemId);
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setMenuItemId(rs.getInt("menu_item_id"));
                    item.setQuantity(rs.getInt("quantity"));

                    MenuItemModel menu = new MenuItemModel();
                    menu.setId(rs.getInt("menu_item_id"));
                    menu.setName(rs.getString("menu_name"));
                    item.setMenuItem(menu);

                    order.getItems().add(item);
                }
            }

            return order;
        });
    }


}
