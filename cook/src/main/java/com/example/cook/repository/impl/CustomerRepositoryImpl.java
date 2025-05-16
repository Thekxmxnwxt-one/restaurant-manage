package com.example.cook.repository.impl;

import com.example.cook.model.CustomersModel;
import com.example.cook.model.TablesModel;
import com.example.cook.repository.CustomerNativeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class CustomerRepositoryImpl implements CustomerNativeRepository {
    private JdbcTemplate jdbcTemplate;

    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<CustomersModel> findAllCustomer() {
        String sql = "SELECT id, name, phone, table_id, status, created_at FROM customers";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomersModel customer = new CustomersModel();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setPhone(rs.getString("phone"));
            customer.setTablesId(rs.getInt("table_id"));
            customer.setStatus(rs.getString("status"));
            customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return customer;
        });
    }

    @Override
    public List<CustomersModel> findAllActiveCustomer() {
        String sql = """
            SELECT id, name, phone, table_id, status, created_at
            FROM customers
            WHERE status IN ('seated', 'waiting')
            ORDER BY created_at DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomersModel customer = new CustomersModel();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setPhone(rs.getString("phone"));
            customer.setTablesId(rs.getInt("table_id"));
            customer.setStatus(rs.getString("status"));
            customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return customer;
        });
    }

    @Override
    public CustomersModel findCustomerByCustomerId(int customerId) {
        String sql = """
        SELECT 
            c.id AS customer_id,
            c.name AS customer_name,
            c.phone AS customer_phone,
            c.table_id AS customer_tableId,
            c.status AS customer_status,
            c.created_at AS customer_created_at,
            t.id AS table_id,
            t.table_number,
            t.status AS table_status
        FROM customers c
        LEFT JOIN tables t ON c.table_id = t.id
        WHERE c.id = ?
        ORDER BY c.created_at DESC LIMIT 1
    """;

        List<CustomersModel> result = jdbcTemplate.query(sql, new Object[]{customerId}, (rs, rowNum) -> {
            CustomersModel cs = new CustomersModel();
            cs.setId(rs.getInt("customer_id"));
            cs.setName(rs.getString("customer_name"));
            cs.setPhone(rs.getString("customer_phone"));
            cs.setTablesId(rs.getInt("customer_tableId"));
            cs.setStatus(rs.getString("customer_status"));
            cs.setCreatedAt(rs.getTimestamp("customer_created_at").toLocalDateTime());

            TablesModel tb = new TablesModel();
            tb.setId(rs.getInt("table_id"));
            tb.setTableNumber(rs.getInt("table_number"));
            tb.setStatus(rs.getString("table_status"));

            cs.setTablesModel(tb);
            return cs;
        });

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public int insertCustomer(CustomersModel customersModel) {

        String sql = """
        INSERT INTO customers (name, phone, table_id, status, created_at)
        VALUES (?, ?, ?, ?, NOW())
    """;

        int insertedRow = jdbcTemplate.update(
                sql,
                customersModel.getName(),
                customersModel.getPhone(),
                customersModel.getTablesId(),
                customersModel.getStatus()
        );
        return insertedRow;
    }

    @Override
    public CustomersModel updateStatusCustomer(CustomersModel customersModel) {
        String sql = "UPDATE customers SET status = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql, customersModel.getStatus(), customersModel.getId());

        if (rows > 0) {
            return customersModel;
        } else {
            return null;
        }
    }

}

