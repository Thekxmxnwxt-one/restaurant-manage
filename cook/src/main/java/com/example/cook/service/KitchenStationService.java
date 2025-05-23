package com.example.cook.service;

import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.*;
import com.example.cook.repository.KitchenStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenStationService {
    private final KitchenStationRepository kitchenStationRepository;

    public KitchenStationService(KitchenStationRepository kitchenStationRepository) {
        this.kitchenStationRepository = kitchenStationRepository;
    }

    public List<KitchenStationModel> getAllStation() {
        return kitchenStationRepository.findAllStations();
    }

    public List<OrderItemModel> getStationByName(String stationName) {
        List<OrderItemModel> orders = kitchenStationRepository.findOrdersByStation(stationName);
        if (orders == null || orders.isEmpty()) {
            throw new NotFoundException("No orders found for station: " + stationName);
        }
        return orders;
    }

    public int insertLog(KitchenLogModel kitchenLogModel) {
        int rows = kitchenStationRepository.insertLog(kitchenLogModel);
        if (rows == 0) {
            throw new BadRequestException("Insert failed for log: " + kitchenLogModel);
        }
        return rows;
    }

    public KitchenLogModel updateKitchen(KitchenLogModel kitchenLogModel) {
        KitchenLogModel updated = kitchenStationRepository.updateLogStatus(kitchenLogModel);
        if (updated == null) {
            throw new NotFoundException("Log not found or update failed for: " + kitchenLogModel);
        }
        return updated;
    }
}
