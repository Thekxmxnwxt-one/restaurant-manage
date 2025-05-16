package com.example.cook.repository.impl;

import com.example.cook.model.KitchenLogModel;
import com.example.cook.model.KitchenStationModel;
import com.example.cook.model.MenuItemModel;
import com.example.cook.model.OrderItemModel;
import com.example.cook.repository.KitchenStationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KitchenStationRepositoryImpl implements KitchenStationRepository {
    private JdbcTemplate jdbcTemplate;

    public KitchenStationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<KitchenStationModel> findAllStations() {
        String sql = "SELECT id, name FROM kitchen_stations";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            KitchenStationModel model = new KitchenStationModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            return model;
        });
    }

    @Override
    public List<OrderItemModel> findOrdersByStation(String stationName) {
        String sql = """
            SELECT 
                oi.id AS item_id,
                oi.order_id,
                oi.unit_price,
                oi.menu_item_id,
                oi.quantity,
                oi.status AS item_status,
                oi.kitchen_station,

                m.name AS menu_name,
                m.image_url,
                m.category,
                m.price AS menu_price,

                o.ordered_at,
                o.table_id,
                o.status AS order_status

            FROM order_items oi
            JOIN menu_items m ON oi.menu_item_id = m.id
            JOIN orders o ON oi.order_id = o.id
            WHERE oi.kitchen_station = ?
            ORDER BY o.ordered_at ASC
        """;

        return jdbcTemplate.query(sql, new Object[]{stationName}, (rs, rowNum) -> {
            OrderItemModel item = new OrderItemModel();
            item.setId(rs.getInt("item_id"));
            item.setOrderId(rs.getInt("order_id"));
            item.setUnitPrice(rs.getBigDecimal("unit_price"));
            item.setMenuItemId(rs.getInt("menu_item_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setStatus(rs.getString("item_status"));
            item.setKitchenStation(rs.getString("kitchen_station"));

            MenuItemModel menu = new MenuItemModel();
            menu.setId(rs.getInt("menu_item_id"));
            menu.setName(rs.getString("menu_name"));
            menu.setImageUrl(rs.getString("image_url"));
            menu.setCategory(rs.getString("category"));
            menu.setPrice(rs.getBigDecimal("menu_price"));
            item.setMenuItem(menu);

            return item;
        });
    }

    @Override
    public int insertLog(KitchenLogModel kitchenLogModel) {
        String sql = """
            INSERT INTO kitchen_logs (order_item_id, station_id, status)
            VALUES (?, ?, ?)
        """;
        return jdbcTemplate.update(sql, kitchenLogModel.getOrderItemId(), kitchenLogModel.getStationId(), kitchenLogModel.getStatus());
    }

    @Override
    public KitchenLogModel updateLogStatus(KitchenLogModel kitchenLogModel) {
        String sql = "UPDATE kitchen_logs SET status = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql, kitchenLogModel.getStatus(), kitchenLogModel.getId());

        if (rows > 0) {
            return kitchenLogModel;
        } else {
            return null;
        }
    }
}
