package com.example.cook.repository.impl;

import com.example.cook.entity.UsersEntity;
import com.example.cook.enums.EmployeeRole;
import com.example.cook.model.EmployeesModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.UserNativeRepository;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UsersModel findByUsername(String username) {

        String sql = """ 
                SELECT 
                u.id, 
                u.username, 
                u.password, 
                u.employee_id, 
                u.created_at, 
                e.name, 
                e.role 
                FROM users u 
                JOIN employees e ON u.employee_id = e.id
                WHERE username = ?
             """;

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

                        EmployeesModel emp = new EmployeesModel();
                        emp.setName(rs.getString("name"));
                        emp.setRole(EmployeeRole.valueOf(rs.getString("role")));

                        model.setEmployeesModel(emp);

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
                employee.setRole(EmployeeRole.valueOf(rs.getString("role")));

                return employee;
            }
        });
    }

    public int insertEmployeeWithUser(EmployeesModel employeesModel, String username, String rawPassword) {
        String checkEmployeeSQL = "SELECT id FROM employees WHERE name = ?";
        List<Integer> existingIds = jdbcTemplate.queryForList(
                checkEmployeeSQL,
                new Object[]{employeesModel.getName()},
                Integer.class
        );

        Integer employeeId;

        System.out.println("Checking employee: " + employeesModel.getName());
        System.out.println("Role to insert: " + employeesModel.getRole());

        if (!existingIds.isEmpty()) {
            employeeId = existingIds.get(0);
        } else {
            try {
                PGobject roleObject = new PGobject();
                roleObject.setType("employee_role");
                roleObject.setValue(employeesModel.getRole().toString());

                String insertEmployeeSQL = "INSERT INTO employees (name, role) VALUES (?, ?) RETURNING id";
                employeeId = jdbcTemplate.queryForObject(
                        insertEmployeeSQL,
                        new Object[]{employeesModel.getName(), roleObject},
                        Integer.class
                );
            } catch (Exception e) {
                throw new RuntimeException("Insert employee failed", e);
            }

        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        String insertUserSQL = "INSERT INTO users (username, password, employee_id) VALUES (?, ?, ?)";

        int userInserted = jdbcTemplate.update(
                insertUserSQL,
                username,
                hashedPassword,
                employeeId
        );

        return userInserted;
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
        return count != null && count > 0;
    }


}
