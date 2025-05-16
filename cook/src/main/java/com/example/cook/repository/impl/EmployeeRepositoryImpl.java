package com.example.cook.repository.impl;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.KitchenStationModel;
import com.example.cook.repository.EmployeeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private JdbcTemplate jdbcTemplate;

    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<EmployeesModel> findAllEmployee() {
        String sql = "SELECT id, name, role FROM employees";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmployeesModel model = new EmployeesModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setRole(rs.getString("role"));
            return model;
        });
    }

    @Override
    public EmployeesModel findEmployeeById(int id) {
        String sql = "SELECT id, name, role FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            EmployeesModel model = new EmployeesModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setRole(rs.getString("role"));
            return model;
        });
    }

    @Override
    public int insertEmployee(EmployeesModel employeesModel) {
        String sql = """
        INSERT INTO employees (name, role)
        VALUES (?, ?)
    """;

        int insertedRow = jdbcTemplate.update(
                sql,
                employeesModel.getName(),
                employeesModel.getRole()
        );
        return insertedRow;
    }

    @Override
    public EmployeesModel updateEmployee(EmployeesModel employeesModel) {
        StringBuilder sql = new StringBuilder("UPDATE employees SET ");
        List<Object> params = new ArrayList<>();

        if (employeesModel.getName() != null) {
            sql.append("name = ?, ");
            params.add(employeesModel.getName());
        }

        if (employeesModel.getRole() != null) {
            sql.append("role = ?, ");
            params.add(employeesModel.getRole());
        }

        if (params.isEmpty()) return null;
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        params.add(employeesModel.getId());

        int rows = jdbcTemplate.update(sql.toString(), params.toArray());

        return rows > 0 ? employeesModel : null;
    }

    @Override
    public String deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        if (rows > 0) {
            return "Delete success";
        } else {
            return "Menu not found or already deleted";
        }
    }
}
