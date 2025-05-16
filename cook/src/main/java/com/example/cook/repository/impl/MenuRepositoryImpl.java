package com.example.cook.repository.impl;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.TablesModel;
import com.example.cook.repository.MenuNativeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class MenuRepositoryImpl implements MenuNativeRepository {
    private JdbcTemplate jdbcTemplate;

    public MenuRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MenuItemModel> findAllMenu() {
        String sql = "SELECT id, name, image_url, description, price, category, available FROM menu_items";
        return jdbcTemplate.query(sql, new RowMapper<MenuItemModel>() {
            @Override
            public MenuItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MenuItemModel model = new MenuItemModel();
                model.setId(rs.getInt("id"));
                model.setName(rs.getString("name"));
                model.setImageUrl(rs.getString("image_url"));
                model.setDescription(rs.getString("description"));
                model.setPrice(rs.getBigDecimal("price"));
                model.setCategory(rs.getString("category"));
                model.setAvailable(rs.getBoolean("available"));
                return model;
            }
        });
    }

    @Override
    public MenuItemModel findMenuById(int menuID) {
        String sql = "SELECT id, name, image_url, description, price, category, available FROM menu_items WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{menuID}, new RowMapper<MenuItemModel>(){
            @Override
            public MenuItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MenuItemModel model = new MenuItemModel();
                model.setId(rs.getInt("id"));
                model.setName(rs.getString("name"));
                model.setImageUrl(rs.getString("image_url"));
                model.setDescription(rs.getString("description"));
                model.setPrice(rs.getBigDecimal("price"));
                model.setCategory(rs.getString("category"));
                model.setAvailable(rs.getBoolean("available"));
                return model;
            }
        });
    }

    @Override
    public int insertMenu(List<MenuItemModel> menuItemModels) {
        List<Object> paramList = new ArrayList<>();

        String sql = "INSERT INTO menu_items (name, image_url, description, price, category, available) VALUES";

        StringJoiner stringJoiner = new StringJoiner(",");
        for (MenuItemModel m : menuItemModels){
            String value = " (?, ?, ?, ?, ?, ?) ";
            paramList.add(m.getName());
            paramList.add(m.getImageUrl());
            paramList.add(m.getDescription());
            paramList.add(m.getPrice());
            paramList.add(m.getCategory());
            paramList.add(m.getAvailable());
            stringJoiner.add(value);
        }

        sql += stringJoiner.toString();

        int insertedRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return insertedRow;
    }

    @Override
    public MenuItemModel updateMenu(MenuItemModel menuItemModel) {
        StringBuilder sql = new StringBuilder("UPDATE menu_items SET ");
        List<Object> params = new ArrayList<>();

        if (menuItemModel.getName() != null) {
            sql.append("name = ?, ");
            params.add(menuItemModel.getName());
        }
        if (menuItemModel.getImageUrl() != null) {
            sql.append("image_url = ?, ");
            params.add(menuItemModel.getImageUrl());
        }
        if (menuItemModel.getDescription() != null) {
            sql.append("description = ?, ");
            params.add(menuItemModel.getDescription());
        }
        if (menuItemModel.getPrice() != null) {
            sql.append("price = ?, ");
            params.add(menuItemModel.getPrice());
        }
        if (menuItemModel.getCategory() != null) {
            sql.append("category = ?, ");
            params.add(menuItemModel.getCategory());
        }
        if (menuItemModel.getAvailable() != null) {
            sql.append("available = ?, ");
            params.add(menuItemModel.getAvailable());
        }

        if (params.isEmpty()) return null;
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        params.add(menuItemModel.getId());

        int rows = jdbcTemplate.update(sql.toString(), params.toArray());

        return rows > 0 ? menuItemModel : null;
    }

    @Override
    public String deleteMenu(int id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        if (rows > 0) {
            return "Delete success";
        } else {
            return "Menu not found or already deleted";
        }
    }

}
