package com.example.cook.repository.impl;

import com.example.cook.model.*;
import com.example.cook.repository.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private JdbcTemplate jdbcTemplate;

    public PaymentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PaymentModel> findAllPayments() {
        String sql = "SELECT id, order_id, amount, method, paid_at FROM payments";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PaymentModel payment = new PaymentModel();
            payment.setId(rs.getInt("id"));
            payment.setOrderId(rs.getInt("order_id"));
            payment.setAmount(rs.getBigDecimal("amount"));
            payment.setMethod(rs.getString("method"));
            payment.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            return payment;
        });
    }

    @Override
    public PaymentModel findPaymentById(int id) {
        String sql = "SELECT id, order_id, amount, method, paid_at FROM payments WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            PaymentModel payment = new PaymentModel();
            payment.setId(rs.getInt("id"));
            payment.setOrderId(rs.getInt("order_id"));
            payment.setAmount(rs.getBigDecimal("amount"));
            payment.setMethod(rs.getString("method"));
            payment.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            return payment;
        });
    }

    @Override
    public PaymentModel findPaymentByIdDetail(int id) {
        String sql = """
        SELECT 
            p.id AS payment_id,
            p.order_id,
            p.amount,
            p.method,
            p.paid_at,
            
            o.id AS order_id,
            o.customer_id,
            o.table_id,
            o.employee_id,
            o.ordered_at,
            o.status AS order_status,
            o.closed_at,

            c.name AS customer_name,
            e.name AS employee_name,
            e.role AS employee_role,
            t.table_number AS table_name,

            oi.id AS item_id,
            oi.order_id AS item_order_id,
            oi.menu_item_id,
            oi.unit_price,
            oi.quantity,
            oi.status AS item_status,
            oi.kitchen_station,

            m.name AS menu_name,
            m.price AS menu_price,
            m.category

        FROM payments p
        JOIN orders o ON p.order_id = o.id
        LEFT JOIN customers c ON o.customer_id = c.id
        LEFT JOIN employees e ON o.employee_id = e.id
        LEFT JOIN tables t ON o.table_id = t.id
        LEFT JOIN order_items oi ON o.id = oi.order_id
        LEFT JOIN menu_items m ON oi.menu_item_id = m.id
        WHERE p.id = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            PaymentModel payment = null;
            OrderModel order = null;
            List<OrderItemModel> items = new ArrayList<>();

            while (rs.next()) {
                if (payment == null) {
                    payment = new PaymentModel();
                    payment.setId(rs.getInt("payment_id"));
                    payment.setOrderId(rs.getInt("order_id"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setMethod(rs.getString("method"));
                    payment.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());

                    order = new OrderModel();
                    order.setId(rs.getInt("order_id"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setTableId(rs.getInt("table_id"));
                    order.setEmployeeId(rs.getInt("employee_id"));
                    order.setStatus(rs.getString("order_status"));
                    order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());

                    Timestamp closedAt = rs.getTimestamp("closed_at");
                    order.setClosedAt(closedAt != null ? closedAt.toLocalDateTime() : null);

                    CustomersModel customer = new CustomersModel();
                    customer.setName(rs.getString("customer_name"));
                    order.setCustomer(customer);

                    EmployeesModel employee = new EmployeesModel();
                    employee.setName(rs.getString("employee_name"));
                    employee.setRole(rs.getString("employee_role"));
                    order.setEmployee(employee);

                    TablesModel table = new TablesModel();
                    table.setTableNumber(rs.getInt("table_name"));
                    order.setTable(table);
                }

                int itemId = rs.getInt("item_id");
                if (itemId > 0) {
                    OrderItemModel item = new OrderItemModel();
                    item.setId(itemId);
                    item.setOrderId(rs.getInt("item_order_id"));
                    item.setMenuItemId(rs.getInt("menu_item_id"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setStatus(rs.getString("item_status"));
                    item.setKitchenStation(rs.getString("kitchen_station"));

                    MenuItemModel menu = new MenuItemModel();
                    menu.setId(rs.getInt("menu_item_id"));
                    menu.setName(rs.getString("menu_name"));
                    menu.setPrice(rs.getBigDecimal("menu_price"));
                    menu.setCategory(rs.getString("category"));

                    item.setMenuItem(menu);
                    items.add(item);
                }
            }

            if (order != null) {
                order.setItems(items);
            }
            if (payment != null) {
                payment.setOrder(order);
            }

            return payment;
        });
    }

    @Override
    public int insertPayment(PaymentModel payment) {
        String sql = """
        INSERT INTO payments (order_id, amount, method)
        VALUES (?, ?, ?)
    """;
        return jdbcTemplate.update(
                sql,
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod()
        );
    }

}
