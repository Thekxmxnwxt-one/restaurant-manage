package com.example.cook.repository.impl;

import com.example.cook.entity.UsersEntity;
import com.example.cook.model.EmployeesModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.UserNativeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UsersModel findByUsername(String username) {

        String sql = "SELECT id, username, password, employee_id, created_at FROM users WHERE username = ?";

        List<UsersModel> result = this.jdbcTemplate.query(
                sql, new Object[]{username}, new RowMapper<UsersModel>() {
                    @Override
                    public UsersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UsersModel model = new UsersModel();
                        model.setId(rs.getInt("id"));
                        model.setUsername(rs.getString("username"));
                        model.setPassword(rs.getString("password"));
                        model.setEmployeeId(rs.getInt("employee_id"));
                        model.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        return model;
                    }
                }
        );
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public EmployeesModel findEmployeeById(int employeeId) {
        String sql = "SELECT id, name, role FROM employees WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{employeeId}, new RowMapper<EmployeesModel>() {
            @Override
            public EmployeesModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmployeesModel employee = new EmployeesModel();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setRole(rs.getString("role"));
                return employee;
            }
        });
    }
}
