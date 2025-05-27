package com.example.cook.repository.impl;

import com.example.cook.enums.*;
import com.example.cook.model.*;
import com.example.cook.repository.OrderNativeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderNativeRepository {
    private JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OrderModel> findAllOrder() {
        String sql = """
        SELECT 
            o.id AS order_id,
            o.customer_id,
            o.table_id,
            o.employee_id,
            o.ordered_at,
            o.status AS order_status,
            o.closed_at,
            
            c.name AS customer_name,
            c.phone AS customer_phone,
            c.status,
            c.table_id,
            
            t.table_number,
            t.status AS table_status,
            
            e.name AS employee_name,
            e.role AS employee_role

        FROM orders o
        JOIN customers c ON o.customer_id = c.id
        JOIN tables t ON o.table_id = t.id
        JOIN employees e ON o.employee_id = e.id
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderModel order = new OrderModel();
            order.setId(rs.getInt("order_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setTableId(rs.getInt("table_id"));
            order.setEmployeeId(rs.getInt("employee_id"));
            order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());
            order.setStatus(OrderStatus.valueOf(rs.getString("order_status")));


            Timestamp closedAt = rs.getTimestamp("closed_at");
            if (closedAt != null) {
                order.setClosedAt(closedAt.toLocalDateTime());
            }

            CustomersModel customer = new CustomersModel();
            customer.setId(rs.getInt("customer_id"));
            customer.setName(rs.getString("customer_name"));
            customer.setPhone(rs.getString("customer_phone"));
            customer.setStatus(CustomerStatus.valueOf(rs.getString("status")));
            customer.setTablesId(rs.getInt("table_id"));
            order.setCustomer(customer);

            TablesModel table = new TablesModel();
            table.setId(rs.getInt("table_id"));
            table.setTableNumber(rs.getInt("table_number"));
            table.setStatus(TableStatus.fromDbValue(rs.getString("table_status")));

            order.setTable(table);

            EmployeesModel employee = new EmployeesModel();
            employee.setId(rs.getInt("employee_id"));
            employee.setName(rs.getString("employee_name"));
            employee.setRole(EmployeeRole.valueOf(rs.getString("employee_role")));

            order.setEmployee(employee);

            return order;
        });
    }

    @Override
    public OrderModel findOrderById(int orderId) {
        String sql = """
        SELECT 
            o.id AS order_id,
            o.customer_id,
            o.table_id,
            o.employee_id,
            o.ordered_at,
            o.status AS order_status,
            o.closed_at,

            i.id AS order_item_id,
            i.unit_price,
            i.menu_item_id,
            i.quantity,
            i.status AS item_status,
            i.kitchen_station,

            m.name AS menu_name,
            m.image_url,
            m.price,
            m.category,
            m.available

        FROM orders o
        LEFT JOIN order_items i ON o.id = i.order_id
        LEFT JOIN menu_items m ON i.menu_item_id = m.id
        WHERE o.id = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{orderId}, rs -> {
            OrderModel order = null;
            List<OrderItemModel> itemList = new ArrayList<>();

            while (rs.next()) {
                if (order == null) {
                    order = new OrderModel();
                    order.setId(rs.getInt("order_id"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setTableId(rs.getInt("table_id"));
                    order.setEmployeeId(rs.getInt("employee_id"));
                    order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());
                    order.setStatus(OrderStatus.valueOf(rs.getString("order_status")));

                    Timestamp closedAt = rs.getTimestamp("closed_at");
                    if (closedAt != null) {
                        order.setClosedAt(closedAt.toLocalDateTime());
                    }
                }

                int itemId = rs.getInt("order_item_id");
                if (itemId > 0) {
                    OrderItemModel item = new OrderItemModel();
                    item.setId(itemId);
                    item.setOrderId(orderId);
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setMenuItemId(rs.getInt("menu_item_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setStatus(ItemStatus.valueOf(rs.getString("item_status")));
                    item.setKitchenStation(KitchenstationType.fromDisplayName(rs.getString("kitchen_station")));

                    MenuItemModel menu = new MenuItemModel();
                    menu.setId(rs.getInt("menu_item_id"));
                    menu.setName(rs.getString("menu_name"));
                    menu.setImageUrl(rs.getString("image_url"));
                    menu.setPrice(rs.getBigDecimal("price"));
                    menu.setCategory(MenuCategory.fromDisplayName(rs.getString("category")));
                    menu.setAvailable(rs.getBoolean("available"));

                    item.setMenuItem(menu);
                    itemList.add(item);
                }
            }

            if (order != null) {
                order.setItems(itemList);
            }

            return order;
        });
    }

    @Override
    public int insertOrder(OrderModel orderModels) {
        String sql = """
        INSERT INTO orders (customer_id, table_id, employee_id, ordered_at, status, closed_at)
        VALUES (?, ?, ?, NOW(), ?::order_status, NOW())
    """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // ระบุเฉพาะคอลัมน์ key ที่ต้องการคืนค่า (id)
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setInt(1, orderModels.getCustomerId());
            ps.setInt(2, orderModels.getTableId());
            ps.setInt(3, orderModels.getEmployeeId());
            ps.setString(4, orderModels.getStatus().name());

            return ps;
        }, keyHolder);

        // ดึงค่า id จาก keyHolder ได้อย่างปลอดภัย
        return keyHolder.getKey().intValue();
    }



    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        String sql = "UPDATE orders SET status = ?::order_status WHERE id = ?";

        int rows = jdbcTemplate.update(sql, orderModel.getStatus().name(), orderModel.getId());

        if (rows > 0) {
            return orderModel;
        } else {
            return null;
        }
    }

    @Override
    public String deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        if (rows > 0) {
            return "Delete success";
        } else {
            return "Menu not found or already deleted";
        }
    }

    @Override
    public int insertOrderItem(OrderItemModel orderItemModel) {
        String sql = """
        INSERT INTO order_items (
            order_id,
            unit_price,
            menu_item_id,
            quantity,
            status,
            kitchen_station)
        VALUES (?, ?, ?, ?, 'pending', ?::kitchen_station_type)
    """;
        return jdbcTemplate.update(
                sql,
                orderItemModel.getOrderId(),
                orderItemModel.getUnitPrice(),
                orderItemModel.getMenuItemId(),
                orderItemModel.getQuantity(),
                orderItemModel.getKitchenStation().getDisplayName()
        );
    }

    @Override
    public OrderItemModel updateOrderItems(OrderItemModel orderItemModel) {
        StringBuilder sql = new StringBuilder("UPDATE order_items SET ");
        List<Object> params = new ArrayList<>();

        if (orderItemModel.getMenuItemId() != 0) {
            sql.append("menu_item_id = ?, ");
            params.add(orderItemModel.getMenuItemId());
        }
        if (orderItemModel.getQuantity() != 0) {
            sql.append("quantity = ?, ");
            params.add(orderItemModel.getQuantity());
        }
        if (orderItemModel.getUnitPrice() != null) {
            sql.append("unit_price = ?, ");
            params.add(orderItemModel.getUnitPrice());
        }

        if (sql.toString().endsWith(", ")) {
            sql.setLength(sql.length() - 2);
        }

        sql.append(" WHERE id = ? AND order_id = ?");
        params.add(orderItemModel.getId());
        params.add(orderItemModel.getOrderId());

        int rows = jdbcTemplate.update(sql.toString(), params.toArray());

        if (rows > 0) {
            return orderItemModel;
        } else {
            return null;
        }
    }

    @Override
    public String deleteOrderItem(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        if (rows > 0) {
            return "Delete success";
        } else {
            return "Menu not found or already deleted";
        }
    }

    @Override
    public OrderItemModel updateOrderItemsStatus(OrderItemModel orderItemModel) {
        String sql = "UPDATE order_items SET status = ?::item_status WHERE id = ?";

        int rows = jdbcTemplate.update(sql, orderItemModel.getStatus().name(), orderItemModel.getId());

        if (rows > 0) {
            return orderItemModel;
        } else {
            return null;
        }
    }


}
