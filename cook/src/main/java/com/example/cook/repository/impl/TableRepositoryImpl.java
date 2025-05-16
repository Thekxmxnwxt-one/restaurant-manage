package com.example.cook.repository.impl;

import com.example.cook.model.CustomersModel;
import com.example.cook.model.TablesModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.TableNativeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class TableRepositoryImpl implements TableNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public TableRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TablesModel> findAllTable() {
        String sql = "SELECT id, table_number, status FROM tables";
        return jdbcTemplate.query(sql, new RowMapper<TablesModel>() {
            @Override
            public TablesModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                TablesModel model = new TablesModel();
                model.setId(rs.getInt("id"));
                model.setTableNumber(rs.getInt("table_number"));
                model.setStatus(rs.getString("status"));
                return model;
            }
        });
    }

    @Override
    public TablesModel findTableByTableNumber(int tableNum) {
        String sql = """
        SELECT 
            t.id AS table_id,
            t.table_number,
            t.status AS table_status,
            c.id AS customer_id,
            c.name AS customer_name,
            c.phone AS customer_phone,
            c.table_id AS customer_tableId,
            c.status AS customer_status,
            c.created_at AS customer_created_at
        FROM tables t
        LEFT JOIN customers c ON t.id = c.table_id
        WHERE t.table_number = ?
        ORDER BY c.created_at DESC LIMIT 1
    """;

        List<TablesModel> result = jdbcTemplate.query(sql, new Object[]{tableNum}, (rs, rowNum) -> {
            TablesModel table = new TablesModel();
            table.setId(rs.getInt("table_id"));
            table.setTableNumber(rs.getInt("table_number"));
            table.setStatus(rs.getString("table_status"));

            CustomersModel customer = new CustomersModel();

            if (rs.getObject("customer_id") != null) {
                customer.setId(rs.getInt("customer_id"));
                customer.setName(rs.getString("customer_name"));
                customer.setPhone(rs.getString("customer_phone"));
                customer.setTablesId(rs.getInt("customer_tableId"));
                customer.setStatus(rs.getString("customer_status"));
                customer.setCreatedAt(rs.getTimestamp("customer_created_at").toLocalDateTime());
                customer.setMessage("This table have customer");
            } else {
                customer.setMessage("No Customer");
            }

            table.setCustomer(customer);
            return table;
        });

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public int insertTable(List<TablesModel> tablesModels) {
        List<Object> paramList = new ArrayList<>();

        String sql = "INSERT INTO tables (table_number, status) VALUES";

        StringJoiner stringJoiner = new StringJoiner(",");
        for (TablesModel t : tablesModels){
            String value = " (?, ?) ";
            paramList.add(t.getTableNumber());
            paramList.add(t.getStatus());
            stringJoiner.add(value);
        }

        sql += stringJoiner.toString();

        int insertedRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return insertedRow;
    }

    @Override
    public TablesModel updateTable(TablesModel tablesModel) {
        String sql = "UPDATE tables SET status = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql, tablesModel.getStatus(), tablesModel.getId());

        if (rows > 0) {
            return tablesModel;
        } else {
            return null;
        }
    }

    @Override
    public String deleteTable(int id) {
        String sql = "DELETE FROM tables WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        if (rows > 0) {
            return "Delete success";
        } else {
            return "Menu not found or already deleted";
        }
    }

}