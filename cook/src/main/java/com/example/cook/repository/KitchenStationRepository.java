package com.example.cook.repository;

import com.example.cook.model.KitchenLogModel;
import com.example.cook.model.KitchenStationModel;
import com.example.cook.model.MenuItemModel;
import com.example.cook.model.OrderItemModel;

import java.util.List;

public interface KitchenStationRepository {
    public List<KitchenStationModel> findAllStations();
    public List<OrderItemModel> findOrdersByStation(String stationName);
    public int insertLog(KitchenLogModel kitchenLogModel);
    public KitchenLogModel updateLogStatus(KitchenLogModel kitchenLogModel);
}
